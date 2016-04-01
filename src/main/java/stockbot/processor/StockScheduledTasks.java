package stockbot.processor;

import java.text.ParseException;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import stockbot.model.Operation;
import stockbot.model.StatsQuote;
import stockbot.model.StatsType;

@Component
@Transactional
public class StockScheduledTasks {

	@Autowired
	private QuoteManualExtract extractor;

	private String[] quoteNames = { "BME:ABE", "BME:ANA", "BME:ACX", "BME:ACS", "BME:AENA", "BME:AENA", "BME:AMS",
			"BME:MTS", "BME:POP", "BME:SAB", "BME:SAN", "BME:BKIA", "BME:BKT", "BME:BBVA", "BME:CABK", "BME:DIA",
			"BME:ENG", "BME:ELE", "BME:FCC", "BME:FER", "BME:GAM", "BME:GAS", "BME:GRF", "BME:IAG", "BME:IBE",
			"BME:ITX", "BME:IDR", "BME:MAP", "BME:TL5", "BME:MRL", "BME:OHL", "BME:REE", "BME:REP", "BME:SCYR",
			"BME:TRE", "BME:TEF", "NASDAQ:TSLA", "INDEXBME:IB" };

	// private String[] quoteNames = { "NASDAQ:TSLA" };

	@Scheduled(fixedRate = 86400000)
	public void extractFirstInfo() throws ParseException {
		for (String quote : quoteNames) {
			extractor.extract(quote.split(":")[1], quote);
			System.out.println("Finish load!!!");
			extractor.indicatorsCalulation(quote.split(":")[1]);
			System.out.println("Finish stochastic calculation!!!");
		}
	}

	/*
	 * @Scheduled(fixedRate = 86400000) public void extractStats() throws ParseException,
	 * IOException { for (String quote : quoteNames) { String quoteCode = quote.split(":")[1];
	 * extractor.extractStats(quoteCode, 5); StringBuilder stats = new StringBuilder();
	 * stats.append(writeStats(quoteCode, 1)); stats.append(writeStats(quoteCode, 2));
	 * stats.append(writeStats(quoteCode, 3)); stats.append(writeStats(quoteCode, 4));
	 * stats.append(writeStats(quoteCode, 5)); File file = new File("/home/ivan/Documentos/stats/" +
	 * quoteCode + ".txt"); FileUtils.writeStringToFile(file, stats.toString());
	 * System.out.println("Finish  " + quoteCode); } }
	 */

	private String writeStats(String quote, int days) {
		StatsQuote stats = extractor.extractStats(quote, days);
		StringBuilder result = new StringBuilder("===============================\n");
		result.append(stats.getQuote()).append(' ').append(days).append(" días\n");
		result.append("-------------------------------\n");
		Map<StatsType, Set<Operation>> ops = stats.getOperations();
		ops.forEach((k, v) -> {
			result.append(k.toString()).append(" -> numero de operaciones=").append(v.size());
			result.append(", numero de aciertos=").append(v.stream().filter(o -> o.getProfits() > 0.0D).count());
			result.append(", ganancias por accion=").append(v.stream().mapToDouble(o -> o.getProfits()).sum());
			result.append("\n");
		});
		result.append("===============================\n");
		return result.toString();
	}

	/*
	 * @Scheduled(fixedRate = 86400000) public void extractEveryDayInfo() throws ParseException {
	 * for (String quote : quoteNames) { extractor.extractLast(quote.split(":")[1], quote);
	 * System.out.println("Finish load!!!");
	 * extractor.indicatorsCalulationLast(quote.split(":")[1]);
	 * System.out.println("Finish stochastic calculation!!!"); } }
	 */

}
