package com.example.manufacture.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.manufacture.data.ComponentDAO;
import com.example.manufacture.data.Converters;
import com.example.manufacture.model.Component;

@Database(entities = Component.class, version = 1)
//@TypeConverters(Converters.class)
public abstract class ComponentDatabase extends RoomDatabase {

    private static ComponentDatabase instance;

    public abstract ComponentDAO componentDAO();

    public static synchronized ComponentDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ComponentDatabase.class, "component_table")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}