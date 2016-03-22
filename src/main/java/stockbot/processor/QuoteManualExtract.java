package stockbot.processor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import stockbot.model.Quote;
import stockbot.repository.QuoteRepository;

@Component
public class QuoteManualExtract {

	@Autowired
	private QuoteRepository quoteRepository;

	private final String server = "https://www.google.com";

	public void stochasticCalulation(String quoteName) {
		List<Quote> quotes = quoteRepository.findByQuoteOrderByDateAsc(quoteName);
		List<Double> maxs = new ArrayList<Double>();
		List<Double> mins = new ArrayList<Double>();
		List<Double> averages = new ArrayList<Double>();
		List<Double> ema9 = new ArrayList<Double>();
		List<Double> ema12 = new ArrayList<Double>();
		List<Double> ema26 = new ArrayList<Double>();
		List<Double> earnsAverage = new ArrayList<Double>();
		List<Double> lossesAverage = new ArrayList<Double>();
		double previousClose = 0.0D;
		for (Quote quote : quotes) {
			maxs.add(quote.getHigh());
			mins.add(quote.getLow());
			ema12.add(quote.getClose());
			ema26.add(quote.getClose());
			if (maxs.size() >= 5) {
				quote.setStochastic(getStochastic(getMax(maxs), getMin(mins), quote.getClose()));
				averages.add(quote.getStochastic());
				maxs.remove(0);
				mins.remove(0);
			} else {
				quote.setStochastic(0D);
			}
			if (averages.size() >= 3) {
				quote.setAverage(getAverage(averages, 3));
				averages.remove(0);
			} else {
				quote.setAverage(0D);
			}
			if (ema12.size() >= 12) {
				quote.setEma12(getAverage(ema12, 12));
				ema12.remove(0);
			}
			if (ema26.size() >= 26) {
				quote.setEma26(getAverage(ema26, 26));
				quote.calculateMacd();
				ema9.add(quote.getMacd());
				ema26.remove(0);
			}
			if (ema9.size() >= 9) {
				quote.setEma9(getAverage(ema9, 9));
				ema12.remove(0);
			}
			if (previousClose > 0.0D) {
				double diff = quote.getClose() - previousClose;
				if (diff > 0) {
					earnsAverage.add(diff);
					lossesAverage.add(0.0D);
					quote.setEarns(diff);
					quote.setLosses(0.0D);
				} else {
					earnsAverage.add(0.0D);
					lossesAverage.add(-1 * diff);
					quote.setEarns(0.0D);
					quote.setLosses(diff);
				}
			} else {
				quote.setEarns(0.0D);
				quote.setLosses(0.0D);
			}
			if (earnsAverage.size() >= 10) {
				quote.setEarns_average(getAverage(earnsAverage, 10));
				quote.setLosses(getAverage(lossesAverage, 10));
				earnsAverage.remove(0);
				lossesAverage.remove(0);

			}
			quote.calculateSignal();
			quote.calculateType();
			quote.calculateSignalMacd();
			quote.calculateRSI();
			quote.calculateSignalRSI();
			previousClose = quote.getClose();
		}
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
			Iterable<Quote> objs = quoteRepository.save(quotes);
			objs.iterator();
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
			SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
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
}
