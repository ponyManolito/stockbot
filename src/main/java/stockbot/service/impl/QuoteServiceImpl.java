package stockbot.service.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import stockbot.model.Quote;
import stockbot.service.QuoteService;

@Repository
@Transactional(readOnly = true)
public class QuoteServiceImpl implements QuoteService {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public Quote save(Quote quote) {
		if (quote.getId() == 0) {
			em.persist(quote);
			em.flush();
			return quote;
		} else {
			return em.merge(quote);
		}
	}
}
