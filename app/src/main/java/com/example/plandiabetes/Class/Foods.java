package com.example.plandiabetes.Class;

import java.io.Serializable;

public class Foods implements Serializable {
    private String name;
    private String type;
    Double Cal, Carbs, Sugar, ServingSize;
    public Foods() {
    }

    public Foods(String name, String type, Double cal, Double carbs, Double sugar, Double servingSize) {
        this.name = name;
        this.type = type;
        Cal = cal;
        Carbs = carbs;
        Sugar = sugar;
        ServingSize = servingSize;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Double getCal() {
        return Cal;
    }

    public Double getCarbs() {
        return Carbs;
    }

    public Double getSugar() {
        return Sugar;
    }

    public Double getServingSize() {
        return ServingSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCal(Double cal) {
        Cal = cal;
    }

    public void setCarbs(Double carbs) {
        Carbs = carbs;
    }

    public void setSugar(Double sugar) {
        Sugar = sugar;
    }

    public void setServingSize(Double servingSize) {
        ServingSize = servingSize;
    }
}