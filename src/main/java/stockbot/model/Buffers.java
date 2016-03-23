package stockbot.model;

import java.util.ArrayList;
import java.util.List;

public class Buffers {

	private List<Double> maxs;

	private List<Double> mins;

	private List<Double> averages;

	private List<Double> ema10;

	private List<Double> ema12;

	private List<Double> ema29;

	private List<Double> earnsAverage;

	private List<Double> lossesAverage;

	public Buffers() {
		this.maxs = new ArrayList<Double>();
		this.mins = new ArrayList<Double>();
		this.averages = new ArrayList<Double>();
		this.ema10 = new ArrayList<Double>();
		this.ema12 = new ArrayList<Double>();
		this.ema29 = new ArrayList<Double>();
		this.earnsAverage = new ArrayList<Double>();
		this.lossesAverage = new ArrayList<Double>();
	}

	public List<Double> getMaxs() {
		return maxs;
	}

	public void addMaxs(Double max) {
		this.maxs.add(max);
		if (this.maxs.size() > 5) {
			this.maxs.remove(0);
		}
	}

	public void setMaxs(List<Double> maxs) {
		this.maxs = maxs;
	}

	public List<Double> getMins() {
		return mins;
	}

	public void addMins(Double min) {
		this.mins.add(min);
		if (this.mins.size() > 5) {
			this.mins.remove(0);
		}
	}

	public void setMins(List<Double> mins) {
		this.mins = mins;
	}

	public List<Double> getAverages() {
		return averages;
	}

	public void addAverages(Double average) {
		this.averages.add(average);
		if (this.averages.size() > 3) {
			this.averages.remove(0);
		}
	}

	public void setAverages(List<Double> averages) {
		this.averages = averages;
	}

	public List<Double> getEma10() {
		return ema10;
	}

	public void addEma9(Double ema10) {
		this.ema10.add(ema10);
		if (this.ema10.size() > 10) {
			this.ema10.remove(0);
		}
	}

	public void setEma10(List<Double> ema10) {
		this.ema10 = ema10;
	}

	public List<Double> getEma12() {
		return ema12;
	}

	public void addEma12(Double ema12) {
		this.ema12.add(ema12);
		if (this.ema12.size() > 12) {
			this.ema12.remove(0);
		}
	}

	public void setEma12(List<Double> ema12) {
		this.ema12 = ema12;
	}

	public List<Double> getEma29() {
		return ema29;
	}

	public void addEma29(Double ema29) {
		this.ema29.add(ema29);
		if (this.ema29.size() > 29) {
			this.ema29.remove(0);
		}
	}

	public void setEma29(List<Double> ema29) {
		this.ema29 = ema29;
	}

	public List<Double> getEarnsAverage() {
		return earnsAverage;
	}

	public void addEarnsAverage(Double earnsAverage) {
		this.earnsAverage.add(earnsAverage);
		if (this.earnsAverage.size() > 10) {
			this.earnsAverage.remove(0);
		}
	}

	public void setEarnsAverage(List<Double> earnsAverage) {
		this.earnsAverage = earnsAverage;
	}

	public List<Double> getLossesAverage() {
		return lossesAverage;
	}

	public void addLossesAverage(Double lossesAverage) {
		this.lossesAverage.add(lossesAverage);
		if (this.lossesAverage.size() > 10) {
			this.lossesAverage.remove(0);
		}
	}

	public void setLossesAverage(List<Double> lossesAverage) {
		this.lossesAverage = lossesAverage;
	}

}
