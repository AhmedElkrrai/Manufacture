package com.example.manufacture.data;

import androidx.room.TypeConverter;

import com.example.manufacture.model.Product;
import com.google.gson.Gson;

public class Converters {
    @TypeConverter
    public String fromProductToGSON(Product product) {
        return new Gson().toJson(product);
    }

    @TypeConverter
    public Product fromGSONToProduct(String productString) {
        return new Gson().fromJson(productString, Product.class);
    }
}