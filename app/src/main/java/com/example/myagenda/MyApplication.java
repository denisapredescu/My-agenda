package com.example.myagenda;

import android.app.Application;

import androidx.room.Room;

import com.example.myagenda.database.AppDatabase;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    private static AppDatabase mAppDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mAppDatabase = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static MyApplication getmInstance() {
        return mInstance;
    }

    public static AppDatabase getmAppDatabase() {
        return mAppDatabase;
    }
}