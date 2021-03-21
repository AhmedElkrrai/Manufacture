package com.example.manufacture.model;

public class Consumption {
    private String materialName;
    private String availableAmount;
    private String postProductionAmount;
    private String availableBatches;

    public Consumption(String materialName, String availableAmount, String postProductionAmount, String availableBatches) {
        this.materialName = materialName;
        this.availableAmount = availableAmount;
        this.postProductionAmount = postProductionAmount;
        this.availableBatches = availableBatches;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getPostProductionAmount() {
        return postProductionAmount;
    }

    public void setPostProductionAmount(String postProductionAmount) {
        this.postProductionAmount = postProductionAmount;
    }

    public String getAvailableBatches() {
        return availableBatches;
    }

    public void setAvailableBatches(String availableBatches) {
        this.availableBatches = availableBatches;
    }
}
