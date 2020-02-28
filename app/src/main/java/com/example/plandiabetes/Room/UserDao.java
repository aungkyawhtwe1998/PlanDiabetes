package com.example.plandiabetes.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(UserProfile userProfile);

    @Query("SELECT * FROM user WHERE email=:email")
    List<UserProfile> retrieveStartUser(String email);

}
