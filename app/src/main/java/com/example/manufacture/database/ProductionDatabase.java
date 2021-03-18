package com.example.manufacture.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.manufacture.data.ProductionDAO;
import com.example.manufacture.model.Production;

@Database(entities = Production.class, version = 1)
public abstract class ProductionDatabase extends RoomDatabase {

    private static ProductionDatabase instance;

    public abstract ProductionDAO productionDAO();

    public static synchronized ProductionDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProductionDatabase.class, "production_table")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}