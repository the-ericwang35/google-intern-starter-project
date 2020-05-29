package com.example.foodsupplier.services;

import com.example.foodsupplier.data.FoodSupplierDAO;

public class FoodSupplierService {

    FoodSupplierDAO foodSupplierDAO = new FoodSupplierDAO();

    public String getVendors(String ingredient) {
        String vendors = "";
        try {
            vendors = foodSupplierDAO.getVendors(ingredient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return vendors;
    }
}

