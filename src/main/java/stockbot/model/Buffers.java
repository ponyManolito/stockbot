package stockbot.model;

import java.util.ArrayList;
import java.util.List;

public class Buffers {

	private List<Double> maxs;

	private List<Double> mins;

	private List<Double> averages;

	private List<Double> ema9;

	private List<Double> ema12;

	private List<Double> ema26;

	private List<Double> earnsAverage;

	private List<Double> lossesAverage;

	public Buffers() {
		this.maxs = new ArrayList<Double>();
		this.mins = new ArrayList<Double>();
		this.averages = new ArrayList<Double>();
		this.ema9 = new ArrayList<Double>();
		this.ema12 = new ArrayList<Double>();
		this.ema26 = new ArrayList<Double>();
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

	public List<Double> getEma9() {
		return ema9;
	}

	public void addEma9(Double ema9) {
		this.ema9.add(ema9);
		if (this.ema9.size() > 9) {
			this.ema9.remove(0);
		}
	}

	public void setEma9(List<Double> ema9) {
		this.ema9 = ema9;
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

	public List<Double> getEma26() {
		return ema26;
	}

	public void addEma26(Double ema26) {
		this.ema26.add(ema26);
		if (this.ema26.size() > 26) {
			this.ema26.remove(0);
		}
	}

	public void setEma26(List<Double> ema26) {
		this.ema26 = ema26;
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
