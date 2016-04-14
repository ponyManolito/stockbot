package stockbot.recommendator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import stockbot.model.Quote;
import stockbot.model.QuoteTypes;
import stockbot.model.StatsType;
import stockbot.repository.AppModel;

@Component
public class QuoteRecommendator extends AppModel {

	public String createRecomendation(String qName) {
		List<QuoteTypes> bestTypes = this.quoteTypesRepository.findByQuote(qName);
		Set<String> oscilators = bestTypes == null ? null : bestTypes.stream().map(qt -> qt.getOscilator())
				.collect(Collectors.toSet());
		try {
			Quote lastQuote = this.quoteRepository.findByQuoteOrderByDateDesc(qName, new PageRequest(0, 1)).get(0);
			String strategy = lastQuote.getType().toString();
			boolean isGoodStrategy = oscilators == null ? false : oscilators.contains(strategy);
			if (isGoodStrategy) {
				return qName + ", estando a " + lastQuote.getClose() + ", es buen momento para " + strategy
						+ ", posicion " + lastQuote.getType_macd();
			} else {
				if (strategy.equals(StatsType.TODOS_COMPRAR)) {
					return qName
							+ ", estando a "
							+ lastQuote.getClose()
							+ ", todos los indicadores dice de comprar pero históricamente no es buena opción, posicion "
							+ lastQuote.getType_macd();
				}
				if (strategy.equals(StatsType.TODOS_VENDER)) {
					return qName
							+ ", estando a "
							+ lastQuote.getClose()
							+ ", todos los indicadores dice de comprar pero históricamente no es buena opción, posicion "
							+ lastQuote.getType_macd();
				}
			}
		} catch (Exception e) {
			System.err.println("valor " + qName + ", sin registros");
		}
		return "";
	}

}
