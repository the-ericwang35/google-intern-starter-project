package com.example.foodvendor.services;

import com.example.foodvendor.data.FoodVendorDAO;
import com.example.foodvendor.models.Ingredient;

public class FoodVendorService {

    FoodVendorDAO foodVendorDAO = new FoodVendorDAO();

    public Ingredient getIngredient(int id, String ingredient) {
        Ingredient ingredientRes = null;
        try {
            ingredientRes = foodVendorDAO.getIngredient(id, ingredient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ingredientRes;
    }
}
