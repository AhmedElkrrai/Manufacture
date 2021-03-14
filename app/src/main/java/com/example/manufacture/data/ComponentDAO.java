package com.example.manufacture.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.manufacture.model.Component;
import com.example.manufacture.model.Product;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface ComponentDAO {
    @Insert
    void insert(Component component);

    @Update
    void update(Component component);

    @Delete
    void delete(Component component);

    @Query("SELECT * FROM component_table where componentName = :componentName")
    Observable<Component> get(String componentName);

    @Query("SELECT * FROM component_table")
    LiveData<List<Component>> getAllComponents();
}