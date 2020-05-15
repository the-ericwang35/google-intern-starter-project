package com.example.foodvendor.models;

public class Ingredient {
    private int vendorId;
    private int inventory;
    private int priceInCents;

    public Ingredient(int vendorId, int inventory, int priceInCents) {
        this.vendorId = vendorId;
        this.inventory = inventory;
        this.priceInCents = priceInCents;
    }

    public void decrementInventory() {
        inventory--;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getInventory() {
        return inventory;
    }

    public int getPriceInCents() {
        return priceInCents;
    }
}
