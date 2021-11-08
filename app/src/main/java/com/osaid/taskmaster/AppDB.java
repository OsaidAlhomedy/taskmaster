package com.osaid.taskmaster;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TaskOG.class}, version = 1)
public abstract class AppDB extends RoomDatabase {

    public abstract TaskDAO taskDAO();

    private static AppDB appDB;

    public AppDB() {
    }

    public static AppDB getInstance(Context context) {
        if (appDB == null) {
            appDB = Room.databaseBuilder(context,
                    AppDB.class, "appDB").allowMainThreadQueries().build();
        }
        return appDB;
    }

}
