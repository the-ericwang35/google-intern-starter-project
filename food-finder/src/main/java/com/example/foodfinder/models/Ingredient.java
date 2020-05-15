package com.example.foodfinder.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ingredient {
    private int vendorId;
    private int inventory;
    private int priceInCents;

    @JsonCreator
    public Ingredient(@JsonProperty("vendorId") int vendorId,
                      @JsonProperty("inventory") int inventory,
                      @JsonProperty("priceInCents") int priceInCents) {
        this.vendorId = vendorId;
        this.inventory = inventory;
        this.priceInCents = priceInCents;
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
