package com.example.manufacture.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "component_table")
public class Component {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String componentName;
    private String providerName;
    private String availableAmount;
    private String minAmount;
    private String subscribedProducts;

    public Component(String componentName, String providerName, String availableAmount, String minAmount) {
        this.componentName = componentName;
        this.providerName = providerName;
        this.availableAmount = availableAmount;
        this.minAmount = minAmount;
        this.subscribedProducts = "";
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

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }
}
