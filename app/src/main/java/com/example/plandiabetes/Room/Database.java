package com.example.plandiabetes.Room;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.plandiabetes.Activity.FourthScreen;

@androidx.room.Database(entities = {UserProfile.class},version = 2)
public abstract class Database extends RoomDatabase {
    public static Database instance;
    public abstract UserDao userDao();

    public static Database getDatabase(final Context context){
        if(instance==null){
            synchronized (Database.class){
                if(instance==null){
                    instance= Room.databaseBuilder(context.getApplicationContext(),Database.class,"Diabetes").allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

}
