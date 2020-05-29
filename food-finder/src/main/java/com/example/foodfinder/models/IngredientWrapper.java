package com.example.foodfinder.models;

import java.util.ArrayList;
import java.util.List;

public class IngredientWrapper {
    private List<Ingredient> ingredients;

    public IngredientWrapper(List<Ingredient> ingredients) {
        this.ingredients = new ArrayList<>();
        this.ingredients.addAll(ingredients);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}