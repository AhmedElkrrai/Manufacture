package com.example.manufacture.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.manufacture.model.Product;

import java.util.List;

@Dao
public interface ProductDAO {
    @Insert
    long insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM product_table")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product_table where id = :productId")
    LiveData<Product> getProductById(int productId);

    @Query("SELECT * FROM product_table where productName = :productName")
    LiveData<Product> getProductByName(String productName);
}