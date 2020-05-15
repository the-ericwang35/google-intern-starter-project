package com.example.foodvendor.services;

import com.example.foodvendor.data.FoodVendorDAO;
import com.example.foodvendor.models.Ingredient;

public class FoodVendorService {

    // TODO: Inject this.
    FoodVendorDAO foodVendorDAO = new FoodVendorDAO();

    public Ingredient getIngredient(int id, String ingredient) {
        return foodVendorDAO.getIngredient(id, ingredient);
    }
}
