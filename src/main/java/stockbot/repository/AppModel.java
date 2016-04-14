package stockbot.repository;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AppModel {

	@Autowired
	protected QuoteRepository quoteRepository;

	@Autowired
	protected QuoteTypesRepository quoteTypesRepository;

}
