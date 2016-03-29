package stockbot.model;

import java.util.Date;

public class Operation {
	
	private StatsType type;
	
	private Date date;
	
	private double profits;

	public Operation() {
	}

	public StatsType getType() {
		return type;
	}

	public void setType(StatsType type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getProfits() {
		return profits;
	}

	public void setProfits(double profits) {
		this.profits = profits;
	}

}
