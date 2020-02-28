package com.example.plandiabetes.Class;

import java.io.Serializable;

public class Weight implements Serializable {
    String id,date,time;
    Double weight;

    public Weight() {
    }

    public Weight(String id, String date, String time, Double weight) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
