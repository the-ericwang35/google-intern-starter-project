package com.example.foodsupplier.services;

import com.example.foodsupplier.data.FoodSupplierDAO;

public class FoodSupplierService {

    FoodSupplierDAO foodSupplierDAO = new FoodSupplierDAO();

    public String getVendors(String ingredient) {
        return foodSupplierDAO.getVendors(ingredient);
    }
}

