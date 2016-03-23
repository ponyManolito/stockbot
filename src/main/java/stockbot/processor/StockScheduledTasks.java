package stockbot.processor;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StockScheduledTasks {

	@Autowired
	private QuoteManualExtract extractor;

	private String[] quoteNames = { "BME:ABE", "BME:ANA", "BME:ACX", "BME:ACS", "BME:AENA", "BME:AENA", "BME:AMS",
			"BME:MTS", "BME:POP", "BME:SAB", "BME:SAN", "BME:BKIA", "BME:BKT", "BME:BBVA", "BME:CABK", "BME:DIA",
			"BME:ENG", "BME:ELE", "BME:FCC", "BME:FER", "BME:GAM", "BME:GAS", "BME:GRF", "BME:IAG", "BME:IBE",
			"BME:ITX", "BME:IDR", "BME:MAP", "BME:TL5", "BME:MRL", "BME:OHL", "BME:REE", "BME:REP", "BME:SCYR",
			"BME:TRE", "BME:TEF", "NASDAQ:TSLA" };

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
	 * @Scheduled(fixedRate = 86400000) public void extractEveryDayInfo() throws ParseException {
	 * for (String quote : quoteNames) { extractor.extractLast(quote.split(":")[1], quote);
	 * System.out.println("Finish load!!!");
	 * extractor.indicatorsCalulationLast(quote.split(":")[1]);
	 * System.out.println("Finish stochastic calculation!!!"); } }
	 */

}
