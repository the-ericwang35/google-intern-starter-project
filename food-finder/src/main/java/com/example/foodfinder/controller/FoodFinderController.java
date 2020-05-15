package com.example.foodfinder.controller;

import com.example.foodfinder.models.Ingredient;
import com.example.foodfinder.utils.SpanUtils;
import com.example.foodfinder.services.GetIngredientService;
import com.example.foodfinder.services.GetSuppliersService;
import io.opencensus.common.Scope;
import io.opencensus.trace.AttributeValue;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FoodFinderController {

    private static final Tracer tracer = Tracing.getTracer();

    @GetMapping("/ingredient")
    public List<Ingredient> getIngredient(@RequestParam(value = "ingredient") String ingredient) {

        try (Scope ss = SpanUtils.buildSpan(tracer,"Finding Food")
                .startScopedSpan()) {

            Span span = tracer.getCurrentSpan();

            Map<String, AttributeValue> attributes = new HashMap<>();
            attributes.put("Ingredient", AttributeValue.stringAttributeValue(ingredient));
            span.addAnnotation("Searching for ingredient: ", attributes);

            List<Integer> suppliers = getSuppliers(ingredient);

            return getInventory(ingredient, suppliers);
        }
    }

    private List<Integer> getSuppliers(String ingredient) {
        GetSuppliersService getSuppliersService = new GetSuppliersService();
        return getSuppliersService.getSuppliers(tracer, ingredient);
    }

    private List<Ingredient> getInventory(String ingredient, List<Integer> suppliers) {
        GetIngredientService getIngredientService = new GetIngredientService();
        List<Ingredient> ingredients = new ArrayList<>();
        for (int supplierId : suppliers) {
            Ingredient ingredientInfo = getIngredientService.callSuppliers(tracer, supplierId, ingredient);
            ingredients.add(ingredientInfo);
        }
        return ingredients;

    }

    @GetMapping("/")
    public String home() {
        return "Success";
    }
}
