package com.example.manufacture.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_table")
public class Product {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String productName;
    private String components;
    private boolean lowStock;
    private String stockBatches;

    public Product(String productName, String components) {
        this.productName = productName;
        this.components = components;
        this.stockBatches = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getComponents() {
        return components;
    }

    public void setComponents(String components) {
        this.components = components;
    }

    public boolean isLowStock() {
        return lowStock;
    }

    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
    }

    public String getStockBatches() {
        return stockBatches;
    }

    public void setStockBatches(String stockBatches) {
        this.stockBatches = stockBatches;
    }
}