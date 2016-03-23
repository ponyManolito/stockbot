package stockbot.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import stockbot.model.Quote;

@Transactional
public interface QuoteRepository extends CrudRepository<Quote, Long>, JpaSpecificationExecutor<Quote> {

	List<Quote> findByQuoteOrderByDateAsc(String quote);

	List<Quote> findByQuoteOrderByDateAsc(String quote, Pageable pageRequest);

	List<Quote> findByQuoteOrderByDateDesc(String quoteName, Pageable pageRequest);
}
