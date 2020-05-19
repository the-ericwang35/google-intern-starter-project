package com.example.foodvendor.controller;

import com.example.foodvendor.models.Ingredient;
import com.example.foodvendor.services.FoodVendorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodVendorController {

    FoodVendorService foodVendorService = new FoodVendorService();

    @GetMapping("/inventory")
    public Ingredient getInventory(@RequestParam(value = "id") int id,
                                   @RequestParam(value = "ingredient") String ingredient) {
        return foodVendorService.getIngredient(id, ingredient);
    }

    @GetMapping("/")
    public String home() {
        return "Success";
    }
}
