package stockbot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import stockbot.model.Quote;

@Transactional
public interface QuoteRepository extends CrudRepository<Quote, Long> {

	List<Quote> findByQuoteOrderByDateAsc(String quote);
}
