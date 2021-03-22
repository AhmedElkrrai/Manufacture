package com.example.manufacture.model;

public class ComponentBatch {
    private final String componentName;
    private final String batchesAmount;
    private final String componentAmount;

    public ComponentBatch(String componentName, String batchesAmount, String componentAmount) {
        this.componentName = componentName;
        this.batchesAmount = batchesAmount;
        this.componentAmount = componentAmount;
    }

    public String getComponentAmount() {
        return componentAmount;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getBatchesAmount() {
        return batchesAmount;
    }
}
