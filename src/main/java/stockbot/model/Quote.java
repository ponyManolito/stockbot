package stockbot.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;

@Entity(name = "quote")
public class Quote {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull
	private String quote;

	@NotNull
	private Date date;

	@NotNull
	private double open;

	@NotNull
	private double high;

	@NotNull
	private double low;

	@NotNull
	private double close;

	@NotNull
	private long volume;

	private double stochastic;

	private double average;

	private String position_stochastic;

	private double ema12;

	private double ema29;

	private double ema10;

	private double macd;

	private String type_macd;

	private String position_macd;

	private double earns;

	private double losses;

	private double earns_average;

	private double losses_average;

	private double rsi;

	private String position_rsi;

	private static Gson gson = new Gson();

	public long getId() {
		return id;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public double getStochastic() {
		return stochastic;
	}

	public void setStochastic(double stochastic) {
		this.stochastic = stochastic;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public String getPosition_stochastic() {
		return position_stochastic;
	}

	public void setPosition_stochastic(String position_stochastic) {
		this.position_stochastic = position_stochastic;
	}

	public double getEma12() {
		return ema12;
	}

	public void setEma12(double ema12) {
		this.ema12 = ema12;
	}

	public double getEma29() {
		return ema29;
	}

	public void setEma29(double ema29) {
		this.ema29 = ema29;
	}

	public double getEma10() {
		return ema10;
	}

	public void setEma10(double ema10) {
		this.ema10 = ema10;
	}

	public double getMacd() {
		return macd;
	}

	public void setMacd(double macd) {
		this.macd = macd;
	}

	public String getType_macd() {
		return type_macd;
	}

	public void setType_macd(String type_macd) {
		this.type_macd = type_macd;
	}

	public String getPosition_macd() {
		return position_macd;
	}

	public void setPosition_macd(String position_macd) {
		this.position_macd = position_macd;
	}

	public double getEarns() {
		return earns;
	}

	public void setEarns(double earns) {
		this.earns = earns;
	}

	public double getLosses() {
		return losses;
	}

	public void setLosses(double losses) {
		this.losses = losses;
	}

	public double getEarns_average() {
		return earns_average;
	}

	public void setEarns_average(double earns_average) {
		this.earns_average = earns_average;
	}

	public double getLosses_average() {
		return losses_average;
	}

	public void setLosses_average(double losses_average) {
		this.losses_average = losses_average;
	}

	public double getRsi() {
		return rsi;
	}

	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

	public String getPosition_rsi() {
		return position_rsi;
	}

	public void setPosition_rsi(String position_rsi) {
		this.position_rsi = position_rsi;
	}

	@Override
	public String toString() {
		return gson.toJson(this);
	}

	public void calculateSignal() {
		if (this.average == 0 || this.stochastic == 0) {
			this.position_stochastic = "";
		} else {
			if (this.stochastic > this.average) {
				this.position_stochastic = Signal.COMPRAR.toString();
			} else {
				this.position_stochastic = Signal.VENDER.toString();
			}
		}

	}

	public void calculateMacd() {
		this.macd = this.ema12 - this.ema29;
	}

	public void calculateType() {
		if (this.macd == 0) {
			this.type_macd = "";
		} else {
			if (this.macd > 0) {
				this.type_macd = Signal.LARGA.toString();
			} else {
				this.type_macd = Signal.CORTA.toString();
			}
		}
	}

	public void calculateSignalMacd() {
		if (this.ema10 == 0) {
			this.position_macd = "";
		} else {
			if (this.ema10 > 0) {
				this.position_macd = Signal.COMPRAR.toString();
			} else {
				this.position_macd = Signal.VENDER.toString();
			}
		}
	}

	public void calculateRSI() {
		if (this.earns_average == 0 || this.losses_average == 0) {
			this.rsi = 0.0D;
		} else {
			this.rsi = 100 - (100 / (1 + (this.earns_average / this.losses_average)));
		}
	}

	public void calculateSignalRSI() {
		if (this.rsi > 70.0D) {
			this.position_rsi = Signal.VENDER.toString();
		} else {
			if (this.rsi < 30.0D && this.rsi > 0.0D) {
				this.position_rsi = Signal.COMPRAR.toString();
			} else {
				this.position_rsi = Signal.NEUTRO.toString();
			}
		}
	}

	public StatsType getType() {
		String positions = this.position_macd+"|"+this.position_rsi+"|"+this.position_stochastic;
		int numBuys= StringUtils.countMatches(positions, Signal.COMPRAR.toString());
		switch (numBuys) {
			case 3 :
				return StatsType.TODOS_COMPRAR;
			case 2 :
				int sellPos = StringUtils.indexOf(positions, Signal.VENDER.toString());
				if (sellPos==0){
					return StatsType.RSI_STO_COMPRAR;
				}
				else{
					if (sellPos==8){
						return StatsType.STO_MACD_COMPRAR;
					}
					else{
						return StatsType.RSI_STO_COMPRAR;
					}
				}
				
			default :break;
		}
		int numSells= StringUtils.countMatches(positions,Signal.VENDER.toString());
		switch (numSells) {
			case 3 :
				return StatsType.TODOS_VENDER;
			case 2 :
				int sellPos = StringUtils.indexOf(positions, Signal.COMPRAR.toString());
				if (sellPos==0){
					return StatsType.RSI_STO_VENDER;
				}
				else{
					if (sellPos==7){
						return StatsType.STO_MACD_VENDER;
					}
					else{
						return StatsType.RSI_MACD_VENDER;
					}
				}
				
			default :return null;
		}
	}
}
