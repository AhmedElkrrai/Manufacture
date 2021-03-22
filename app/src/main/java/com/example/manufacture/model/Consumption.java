package com.example.manufacture.model;

public class Consumption {
    private final String materialName;
    private final String availableAmount;
    private final String postProductionAmount;
    private final String availableBatches;

    public Consumption(String materialName, String availableAmount, String postProductionAmount, String availableBatches) {
        this.materialName = materialName;
        this.availableAmount = availableAmount;
        this.postProductionAmount = postProductionAmount;
        this.availableBatches = availableBatches;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public String getPostProductionAmount() {
        return postProductionAmount;
    }

    public String getAvailableBatches() {
        return availableBatches;
    }
}
