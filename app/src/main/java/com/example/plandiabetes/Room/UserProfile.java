package com.example.plandiabetes.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user",indices = @Index(value = {"email"}, unique = true))
public class UserProfile {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "userId")
    private String userId;

    @Ignore
    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "weight")
    private Double weight;

    @ColumnInfo(name = "height")
    private Double height;

    @ColumnInfo(name = "email")
    private String email;

    @Ignore
    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "dob")
    private String dob;

    public UserProfile(String userId, Double weight, Double height, String email, String type, String dob) {
        this.userId = userId;
        this.weight = weight;
        this.height = height;
        this.email = email;
        this.type = type;
        this.dob = dob;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
