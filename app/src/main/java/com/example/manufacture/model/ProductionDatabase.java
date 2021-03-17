package com.example.manufacture.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.manufacture.data.Converters;
import com.example.manufacture.data.ProductionDAO;

@Database(entities = Production.class, version = 1)
@TypeConverters(Converters.class)
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