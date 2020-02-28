package com.example.plandiabetes.Class;

import java.io.Serializable;

public class Medication implements Serializable {
    String mid,name,unit,power,date,time,note;

    public Medication() {
    }

    public Medication(String mid, String name, String unit, String power, String date, String time, String note) {
        this.mid = mid;
        this.name = name;
        this.unit = unit;
        this.power = power;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
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
}
