package com.example.manufacture.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "production_table")
public class Production {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private final int productID;
    private final String productionEnrolmentDate;
    private final String patchNumber;
    private String productName;

    public Production(int productID, String productionEnrolmentDate, String patchNumber, String productName) {
        this.productID = productID;
        this.productionEnrolmentDate = productionEnrolmentDate;
        this.patchNumber = patchNumber;
        this.productName = productName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductionEnrolmentDate() {
        return productionEnrolmentDate;
    }

    public String getPatchNumber() {
        return patchNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
