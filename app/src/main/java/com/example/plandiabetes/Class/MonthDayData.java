package com.example.plandiabetes.Class;

public class MonthDayData {
    String month;
    Double sales;

    public MonthDayData(String month, Double sales) {
        this.month = month;
        this.sales = sales;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getSales() {
        return sales;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }
}
