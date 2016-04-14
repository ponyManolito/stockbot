package stockbot.processor;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import stockbot.model.Operation;
import stockbot.model.QuoteTypes;
import stockbot.model.StatsQuote;
import stockbot.model.StatsType;
import stockbot.recommendator.QuoteRecommendator;

@Component
@Transactional
public class StockScheduledTasks {

	@Autowired
	private QuoteManualExtract extractor;

	@Autowired
	private QuoteRecommendator recommendator;

	private String[] quoteNames = { "BME:ABE", "BME:ANA", "BME:ACX", "BME:ACS", "BME:AENA", "BME:AENA", "BME:AMS",
			"BME:MTS", "BME:POP", "BME:SAB", "BME:SAN", "BME:BKIA", "BME:BKT", "BME:BBVA", "BME:CABK", "BME:DIA",
			"BME:ENG", "BME:ELE", "BME:FCC", "BME:FER", "BME:GAM", "BME:GAS", "BME:GRF", "BME:IAG", "BME:IBE",
			"BME:ITX", "BME:IDR", "BME:MAP", "BME:TL5", "BME:MRL", "BME:OHL", "BME:REE", "BME:REP", "BME:SCYR",
			"BME:TRE", "BME:TEF", "NASDAQ:TSLA", "INDEXBME:IB" };

	// private String[] quoteNames = { "NASDAQ:TSLA" };

	@Scheduled(fixedRate = 86400000)
	public void extractFirstInfo() throws ParseException {
		for (String quote : quoteNames) {
			String qName = quote.split(":")[1];
			/*
			 * extractor.extract(qName, quote); System.out.println("Finish load!!!");
			 * extractor.indicatorsCalulation(qName);
			 * System.out.println("Finish stochastic calculation!!!"); //
			 */
			System.out.println(recommendator.createRecomendation(qName));
		}
	}

	/*
	 * @Scheduled(fixedRate = 86400000) public void extractStats() throws ParseException,
	 * IOException { Map<String, Set<QuoteTypes>> mapBestStrategies = new HashMap<>(); for (String
	 * quote : quoteNames) { String quoteCode = quote.split(":")[1];
	 * mapBestStrategies.put(quoteCode, extractor.getStrategies(quoteCode)); //
	 * extractor.extractStats(quoteCode, 5); StringBuilder stats = new StringBuilder();
	 * stats.append(writeStats(quoteCode, 1, mapBestStrategies)); stats.append(writeStats(quoteCode,
	 * 2, mapBestStrategies)); stats.append(writeStats(quoteCode, 3, mapBestStrategies));
	 * stats.append(writeStats(quoteCode, 4, mapBestStrategies)); stats.append(writeStats(quoteCode,
	 * 5, mapBestStrategies)); File file = new File("/home/ivange/Documentos/stats/" + quoteCode +
	 * ".txt"); FileUtils.writeStringToFile(file, stats.toString()); System.out.println("Finish  " +
	 * quoteCode); } extractor.saveStrategies(mapBestStrategies); }
	 */

	private String writeStats(String quote, int days, Map<String, Set<QuoteTypes>> mapBestStrategies) {
		StatsQuote stats = extractor.extractStats(quote, days);
		StringBuilder result = new StringBuilder("===============================\n");
		result.append(stats.getQuote()).append(' ').append(days).append(" días\n");
		result.append("-------------------------------\n");
		Map<StatsType, Set<Operation>> ops = stats.getOperations();
		ops.forEach((k, v) -> {
			int size = v.size();
			long success = v.stream().filter(o -> o.getProfits() > 0.0D).count();
			double profits = v.stream().mapToDouble(o -> o.getProfits()).sum();
			result.append(k.toString()).append(" -> numero de operaciones=").append(size);
			result.append(", numero de aciertos=").append(success);
			result.append(", ganancias por accion=").append(profits);
			result.append("\n");
			checkGoodStrategy(size, success, profits, k, quote, mapBestStrategies);
		});
		result.append("===============================\n");
		return result.toString();
	}

	private void checkGoodStrategy(int size, long success, double profits, StatsType k, String quote,
			Map<String, Set<QuoteTypes>> mapBestStrategies) {
		double average = (success * 100) / size;

		if (average > 60.0D && profits > 0.0D) {
			Set<QuoteTypes> oscilators = mapBestStrategies.get(quote);
			if (oscilators == null) {
				oscilators = new HashSet<>();
			}
			oscilators.add(new QuoteTypes(quote, k.toString()));
		}

	}

	/*
	 * @Scheduled(fixedRate = 86400000) public void extractEveryDayInfo() throws ParseException { //
	 * StringBuilder recommendation = new StringBuilder(""); for (String quote : quoteNames) {
	 * String qName = quote.split(":")[1]; int news = extractor.extractLast(qName, quote);
	 * System.out.println("Finish load!!!"); if (news > 0) {
	 * extractor.indicatorsCalulationLast(qName, news);
	 * System.out.println("Finish stochastic calculation!!!"); } //
	 * recommendation.append(recommendator.createRecomendation(qName)); } //
	 * System.out.println(recommendation.toString()); }
	 */

}
