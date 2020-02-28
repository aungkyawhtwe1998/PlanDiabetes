package com.example.plandiabetes.Class;

import java.io.Serializable;

public class Exercise implements Serializable {
    String id,date,time,type,note;
    Double cal;
    Integer minute;

    public Exercise(String id, String date, String time, String type,String note, Double cal, Integer minute) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.note = note;
        this.cal = cal;
        this.minute = minute;
    }

    public Exercise() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getCal() {
        return cal;
    }

    public void setCal(Double cal) {
        this.cal = cal;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }
}
