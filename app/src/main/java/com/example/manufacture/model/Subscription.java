package com.example.manufacture.model;

public class Subscription {
    private final Product product;
    private final boolean lowStock;

    public Subscription(Product product, boolean lowStock) {
        this.product = product;
        this.lowStock = lowStock;
    }

    public Product getProduct() {
        return product;
    }

    public boolean isLowStock() {
        return lowStock;
    }
}
