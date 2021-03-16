package com.example.manufacture.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.manufacture.model.Component;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ComponentDAO {
    @Insert
    long insert(Component component);

    @Update
    void update(Component component);

    @Delete
    void delete(Component component);

    @Query("SELECT * FROM component_table")
    LiveData<List<Component>> getAllComponents();

    @Query("SELECT * FROM component_table where id = :componentId")
    LiveData<Component> getComponentById(int componentId);
}