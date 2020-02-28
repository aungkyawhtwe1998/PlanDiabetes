package com.example.plandiabetes.Class;

public class FireUser {
    String userid,email,password,type,dob,gender;
    Double weight,height;

    public FireUser(String userid, String email, String password, String type, String dob, Double weight, Double height,String gender) {
        this.userid = userid;
        this.email = email;
        this.password = password;
        this.type = type;
        this.dob = dob;
        this.weight = weight;
        this.height = height;
        this.gender= gender;
    }

    public FireUser() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}
