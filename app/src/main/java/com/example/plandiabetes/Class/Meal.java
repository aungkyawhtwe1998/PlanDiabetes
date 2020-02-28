package com.example.plandiabetes.Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meal implements Serializable {
    String id,date,time,note;

    Double totcarb,totcal;


    public Meal() {
    }

    public Meal(String id, String date, String time, String note, Double totcarb) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.note = note;
        this.totcarb = totcarb;

    }

    public Double getTotcal() {
        return totcal;
    }

    public void setTotcal(Double totcal) {
        this.totcal = totcal;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getTotcarb() {
        return totcarb;
    }

    public void setTotcarb(Double totcarb) {
        this.totcarb = totcarb;
    }
}
