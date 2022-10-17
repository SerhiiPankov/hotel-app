package hotel.model;

import java.math.BigDecimal;

public class Currency {
    private int currencyCodeA;
    private int currencyCodeB;
    private long dateTime;
    private BigDecimal rateBuy;
    private BigDecimal rateSell;

    public int getCurrencyCodeA() {
        return currencyCodeA;
    }

    public void setCurrencyCodeA(int currencyCodeA) {
        this.currencyCodeA = currencyCodeA;
    }

    public int getCurrencyCodeB() {
        return currencyCodeB;
    }

    public void setCurrencyCodeB(int currencyCodeB) {
        this.currencyCodeB = currencyCodeB;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getRateBuy() {
        return rateBuy;
    }

    public void setRateBuy(BigDecimal rateBuy) {
        this.rateBuy = rateBuy;
    }

    public BigDecimal getRateSell() {
        return rateSell;
    }

    public void setRateSell(BigDecimal rateSell) {
        this.rateSell = rateSell;
    }

    @Override
    public String toString() {
        return "Currency{"
                + "currencyCodeA=" + currencyCodeA
                + ", currencyCodeB=" + currencyCodeB
                + ", dateTime=" + dateTime
                + '}';
    }
}
