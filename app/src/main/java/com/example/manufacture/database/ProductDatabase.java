package com.example.manufacture.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.manufacture.data.ProductDAO;
import com.example.manufacture.model.Product;

@Database(entities = Product.class, version = 1)
public abstract class ProductDatabase extends RoomDatabase {

    private static ProductDatabase instance;

    public abstract ProductDAO productDAO();

    public static synchronized ProductDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProductDatabase.class, "product_table")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}