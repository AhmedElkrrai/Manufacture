package com.example.manufacture.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.manufacture.model.Product;
import com.example.manufacture.model.Production;

import java.util.List;

@Dao
public interface ProductionDAO {
    @Insert
    long insert(Production production);

    @Update
    void update(Production production);

    @Delete
    void delete(Production production);

    @Query("SELECT * FROM production_table")
    LiveData<List<Production>> getAllProductions();

    @Query("SELECT * FROM production_table where id = :productionId")
    LiveData<Production> getProductionById(int productionId);

    @Query("SELECT * FROM production_table where productID = :productID")
    LiveData<List<Production>> getProductionsByProductId(int productID);
}