package stockbot.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatsQuote {
	
	private String quote;
	
	private Map<StatsType,Set<Operation>> operations; 

	public StatsQuote() {
		quote="";
		operations = new HashMap<>();
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public Map<StatsType, Set<Operation>> getOperations() {
		return operations;
	}

	public void setOperations(Map<StatsType, Set<Operation>> operations) {
		this.operations = operations;
	}
	
	public void addOperations(StatsType type, Operation operation) {
		Set<Operation> ops = this.operations.get(type);
		if (ops==null){
			ops = new HashSet<>();
		}
		ops.add(operation);
		this.operations.put(type, ops);
	}
	
}
