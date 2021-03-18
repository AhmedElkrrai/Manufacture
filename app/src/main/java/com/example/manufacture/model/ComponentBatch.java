package com.example.manufacture.model;

public class ComponentBatch {
    private String componentName;
    private String batchesAmount;
    private String componentAmount;

    public ComponentBatch(String componentName, String batchesAmount, String componentAmount) {
        this.componentName = componentName;
        this.batchesAmount = batchesAmount;
        this.componentAmount = componentAmount;
    }

    public String getComponentAmount() {
        return componentAmount;
    }

    public void setComponentAmount(String componentAmount) {
        this.componentAmount = componentAmount;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getBatchesAmount() {
        return batchesAmount;
    }

    public void setBatchesAmount(String batchesAmount) {
        this.batchesAmount = batchesAmount;
    }
}
