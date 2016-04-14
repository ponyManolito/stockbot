package stockbot.processor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import stockbot.model.Buffers;
import stockbot.model.Operation;
import stockbot.model.Quote;
import stockbot.model.QuoteTypes;
import stockbot.model.Signal;
import stockbot.model.StatsQuote;
import stockbot.repository.AppModel;

@Component
public class QuoteManualExtract extends AppModel {

	private final String server = "https://www.google.com";

	public void indicatorsCalulation(String quoteName) {
		List<Quote> quotes = quoteRepository.findByQuoteOrderByDateAsc(quoteName);
		operations(quotes, new Buffers());
	}

	private Double getMin(List<Double> mins) {
		Double result = 10000000000000D;
		for (Double min : mins) {
			if (min.compareTo(result) < 0) {
				result = min;
			}
		}
		return result;
	}

	private Double getMax(List<Double> maxs) {
		Double result = -10000000000000D;
		for (Double min : maxs) {
			if (min.compareTo(result) > 0) {
				result = min;
			}
		}
		return result;
	}

	private double getAverage(List<Double> averages, int num) {
		Double sum = 0D;
		for (Double average : averages) {
			sum += average;
		}
		return sum / num;
	}

	private double getStochastic(Double maxHighs, Double minLows, Double close) {
		return close == null ? 0D : (((close - minLows) / (maxHighs - minLows)) * 100);
	}

	public void extract(String quoteName, String qCode) throws ParseException {
		try {
			String stockPage = server + "/finance/historical?q=" + qCode;
			Document doc = Jsoup.connect(stockPage).post();
			Elements elements = doc.select(".fjfe-nav-sub").select("a");
			Iterator<Element> linksIterator = elements.iterator();
			linksIterator.next();
			List<Quote> quotes = new ArrayList<Quote>();
			quotes.addAll(extractQuotes(quoteName, stockPage + "&start=0&num=200", doc));
			quotes.addAll(extractQuotes(quoteName, stockPage + "&start=200&num=400", doc));
			quoteRepository.save(quotes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Quote> extractQuotes(String quote, String url, Document doc) throws IOException, ParseException {

		doc = Jsoup.connect(url).get();
		Elements elements = doc.select(".gf-table").select("tr");
		Iterator<Element> trsIterator = elements.iterator();
		trsIterator.next();
		List<Quote> quotes = new ArrayList<Quote>();

		while (trsIterator.hasNext()) {
			Quote model = new Quote();
			model.setQuote(quote);
			Iterator<Element> tds = trsIterator.next().select("td").iterator();
			SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
			model.setDate(df2.parse(tds.next().html()));
			model.setOpen(Double.parseDouble(tds.next().html().replace(",", "")));
			model.setHigh(Double.parseDouble(tds.next().html().replace(",", "")));
			model.setLow(Double.parseDouble(tds.next().html().replace(",", "")));
			model.setClose(Double.parseDouble(tds.next().html().replace(",", "")));
			model.setVolume(Long.parseLong(tds.next().html().replace(",", "")));
			quotes.add(model);
		}
		return quotes;
	}

	public int extractLast(String quoteName, String qCode) throws ParseException {
		try {
			String stockPage = server + "/finance/historical?q=" + qCode;
			Document doc = Jsoup.connect(stockPage).post();
			Elements elements = doc.select(".fjfe-nav-sub").select("a");
			Iterator<Element> linksIterator = elements.iterator();
			linksIterator.next();
			List<Quote> quotes = new ArrayList<Quote>();
			quotes.addAll(extractQuotes(quoteName, stockPage + "&start=0&num=5", doc));
			Quote last = quoteRepository.findByQuoteOrderByDateDesc(quoteName, new PageRequest(0, 1)).get(0);
			Set<Quote> toSave = new HashSet<>();
			for (Quote quote : quotes) {
				if (last.getDate().compareTo(quote.getDate()) < 0) {
					toSave.add(quote);
				}
			}
			quoteRepository.save(toSave);
			return toSave.size();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void indicatorsCalulationLast(String quoteName, int news) {
		List<Quote> quotes = quoteRepository.findByQuoteOrderByDateDesc(quoteName, new PageRequest(0, 26 + news));
		List<Quote> newQuotes = new ArrayList<>();
		newQuotes.add(quotes.get(0));
		quotes.remove(0);
		Collections.reverse(quotes);
		Buffers buffers = new Buffers();
		for (Quote quote : quotes) {
			buffers.addAverages(quote.getAverage());
			buffers.addAverages(quote.getAverage());
			buffers.addEarnsAverage(quote.getEarns_average());
			buffers.addEma12(quote.getClose());
			buffers.addEma26(quote.getClose());
			buffers.addEma10(quote.getEma10());
			buffers.addLossesAverage(quote.getLosses_average());
			buffers.addMaxs(quote.getHigh());
			buffers.addMins(quote.getLow());
		}
		operations(newQuotes, buffers);
		quoteRepository.save(newQuotes);
	}

	private void operations(List<Quote> quotes, Buffers buffers) {
		double previousClose = 0.0D;
		for (Quote quote : quotes) {
			buffers.addMaxs(quote.getHigh());
			buffers.addMins(quote.getLow());
			buffers.addEma12(quote.getClose());
			buffers.addEma26(quote.getClose());
			if (buffers.getMaxs().size() >= 14) {
				quote.setStochastic(getStochastic(getMax(buffers.getMaxs()), getMin(buffers.getMins()),
						quote.getClose()));
				buffers.addAverages(quote.getStochastic());
			} else {
				quote.setStochastic(0D);
			}
			if (buffers.getAverages().size() >= 3) {
				quote.setAverage(getAverage(buffers.getAverages(), 3));
			} else {
				quote.setAverage(0D);
			}
			if (buffers.getEma12().size() >= 12) {
				quote.setEma12(getAverage(buffers.getEma12(), 12));
			}
			if (buffers.getEma26().size() >= 26) {
				quote.setEma26(getAverage(buffers.getEma26(), 26));
				quote.calculateMacd();
				buffers.addEma10(quote.getMacd());
			}
			if (buffers.getEma10().size() >= 10) {
				quote.setEma10(getAverage(buffers.getEma10(), 10));
			}
			if (previousClose > 0.0D) {
				double diff = quote.getClose() - previousClose;
				if (diff > 0) {
					buffers.addEarnsAverage(diff);
					buffers.addLossesAverage(0.0D);
					quote.setEarns(diff);
					quote.setLosses(0.0D);
				} else {
					buffers.addEarnsAverage(0.0D);
					buffers.addLossesAverage(-1 * diff);
					quote.setEarns(0.0D);
					quote.setLosses(-1 * diff);
				}
			} else {
				quote.setEarns(0.0D);
				quote.setLosses(0.0D);
			}
			if (buffers.getEarnsAverage().size() >= 10) {
				quote.setEarns_average(getAverage(buffers.getEarnsAverage(), 10));
				quote.setLosses_average(getAverage(buffers.getLossesAverage(), 10));

			}
			quote.calculateSignal();
			quote.calculateType();
			quote.calculateSignalMacd();
			quote.calculateRSI();
			quote.calculateSignalRSI();
			previousClose = quote.getClose();
		}
	}

	public StatsQuote extractStats(String quoteName, int days) {

		List<Quote> quotes = quoteRepository.findByQuoteOrderByDateAsc(quoteName);

		StatsQuote stats = createStats(
				quoteName,
				quotes.stream().filter(
						q -> StringUtils.isNoneBlank(q.getPosition_rsi())
								&& StringUtils.isNoneBlank(q.getPosition_macd())
								&& StringUtils.isNoneBlank(q.getPosition_stochastic())), days);

		return stats;
	}

	private StatsQuote createStats(String quoteName, Stream<Quote> quotes, int days) {
		StatsQuote stats = new StatsQuote();
		stats.setQuote(quoteName);

		List<Operation> ops = new ArrayList<>();

		quotes.forEach(quote -> {
			processQuote(quote, ops, stats, days);
		});

		return stats;
	}

	private void processQuote(Quote quote, List<Operation> ops, StatsQuote stats, int days) {
		if (ops.size() >= days) {
			Operation closeOp = ops.get(0);
			if (StringUtils.indexOf(closeOp.getType().toString(), Signal.COMPRAR.toString()) > -1) {
				closeOp.setProfits(closeOp.getProfits() + quote.getClose());
			} else {
				closeOp.setProfits(closeOp.getProfits() - quote.getClose());
			}
			stats.addOperations(closeOp.getType(), closeOp);
			ops.remove(0);
		}
		Operation op = new Operation();
		op.setDate(quote.getDate());
		op.setType(quote.getType());
		if (op.getType() != null) {
			if (StringUtils.indexOf(op.getType().toString(), Signal.COMPRAR.toString()) > -1) {
				op.setProfits(quote.getClose() * -1);
			} else {
				op.setProfits(quote.getClose());
			}
			stats.addOperations(quote.getType(), op);
			ops.add(op);
		}
	}

	public Set<QuoteTypes> getStrategies(String quoteCode) {
		return this.quoteTypesRepository.findByQuote(quoteCode).parallelStream().collect(Collectors.toSet());
	}

	public void saveStrategies(Map<String, Set<QuoteTypes>> mapBestStrategies) {
		mapBestStrategies.forEach((k, v) -> {
			this.quoteTypesRepository.save(v);
		});

	}
}
