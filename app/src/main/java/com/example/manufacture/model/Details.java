package com.example.manufacture.model;

public class Details {

    private String componentName;
    private String providerName;
    private String availableAmount;
    private String minAmount;
    private String productAmount;

    public Details(String componentName, String providerName, String availableAmount, String minAmount, String productAmount) {
        this.componentName = componentName;
        this.providerName = providerName;
        this.availableAmount = availableAmount;
        this.minAmount = minAmount;
        this.productAmount = productAmount;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }
}
