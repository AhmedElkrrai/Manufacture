package com.example.manufacture.model;

public class Details {

    private final String componentName;
    private final String availableAmount;
    private final String productAmount;

    public Details(String componentName, String availableAmount, String productAmount) {
        this.componentName = componentName;
        this.availableAmount = availableAmount;
        this.productAmount = productAmount;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public String getProductAmount() {
        return productAmount;
    }
}
