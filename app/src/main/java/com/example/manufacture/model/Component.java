package com.example.manufacture.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;

@Entity(tableName = "component_table")
public class Component {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String componentName;
    private String providerName;
    private String availableAmount;
    private String subscribedProducts;
    private String stockBatches;

    private boolean lowStock;

    public Component(String componentName, String providerName, String availableAmount) {
        this.componentName = componentName;
        this.providerName = providerName;
        this.availableAmount = availableAmount;
        this.subscribedProducts = "";
        this.stockBatches = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubscribedProducts() {
        return subscribedProducts;
    }

    public void setSubscribedProducts(String subscribedProducts) {
        this.subscribedProducts = subscribedProducts;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
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