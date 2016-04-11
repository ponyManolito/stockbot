package stockbot.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "quote_types")
public class QuoteTypes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3066755903253874789L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private String quote;

	@NotNull
	private String oscilator;

	public QuoteTypes() {
		super();
	}

	public QuoteTypes(String quote, String oscilator) {
		super();
		this.quote = quote;
		this.oscilator = oscilator;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getOscilator() {
		return oscilator;
	}

	public void setOscilator(String oscilator) {
		this.oscilator = oscilator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((oscilator == null) ? 0 : oscilator.hashCode());
		result = prime * result + ((quote == null) ? 0 : quote.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuoteTypes other = (QuoteTypes) obj;
		if (oscilator == null) {
			if (other.oscilator != null)
				return false;
		} else if (!oscilator.equals(other.oscilator))
			return false;
		if (quote == null) {
			if (other.quote != null)
				return false;
		} else if (!quote.equals(other.quote))
			return false;
		return true;
	}

}
