package com.example.plandiabetes.Class;

import android.text.Editable;

import java.io.Serializable;

public class Glucose implements Serializable {
    String id,date,time,period,notes,unit;
    Double glucoseLevel;

    public Glucose() {
    }

    public Glucose(String id, String date, String time, String period, String notes, String unit, Double glucoseLevel) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.period = period;
        this.notes = notes;
        this.unit = unit;
        this.glucoseLevel = glucoseLevel;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getGlucoseLevel() {
        return glucoseLevel;
    }

    public void setGlucoseLevel(Double glucoseLevel) {
        this.glucoseLevel = glucoseLevel;
    }
}
