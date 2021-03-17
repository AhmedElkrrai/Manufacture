package com.example.manufacture.model;

public class Details {

    private final String componentName;
    private final String availableAmount;
    private final String minAmount;
    private final String productAmount;

    public Details(String componentName, String availableAmount, String minAmount, String productAmount) {
        this.componentName = componentName;
        this.availableAmount = availableAmount;
        this.minAmount = minAmount;
        this.productAmount = productAmount;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public String getProductAmount() {
        return productAmount;
    }
}
