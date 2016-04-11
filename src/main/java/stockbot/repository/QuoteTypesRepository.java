package stockbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import stockbot.model.QuoteTypes;

@Transactional
public interface QuoteTypesRepository extends CrudRepository<QuoteTypes, Long>, JpaSpecificationExecutor<QuoteTypes> {

	List<QuoteTypes> findByQuote(String quote);

}
