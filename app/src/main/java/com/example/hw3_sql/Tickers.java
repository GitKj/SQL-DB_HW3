package com.example.hw3_sql;

public class Tickers {

    private String tickerName;
    private String companyName;
    private String transType;
    private String PPS;
    private String transactionAmount;
    private String orderType;
    private String confirmationCode;

    public Tickers()
    {}

    public Tickers(String tickerName, String companyName, String transType, String PPS, String transactionAmount, String orderType, String confirmationCode) {
        this.tickerName = tickerName;
        this.companyName = companyName;
        this.transType = transType;
        this.PPS = PPS;
        this.transactionAmount = transactionAmount;
        this.orderType = orderType;
        this.confirmationCode = confirmationCode;
    }

    public String getTickerName() {
        return tickerName;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPPS() {
        return PPS;
    }

    public void setPPS(String PPS) {
        this.PPS = PPS;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }
}
