package com.example.foodvendor.data;

import com.example.foodvendor.models.Ingredient;
import io.opencensus.trace.AttributeValue;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.samplers.Samplers;

import java.util.HashMap;
import java.util.Map;

public class FoodVendorDAO {

    private static final String APPLE = "apple";
    private static final String BANANA = "banana";
    private static final int VENDOR_ONE = 1;
    private static final int VENDOR_TWO = 2;
    private static final int VENDOR_THREE = 3;

    private Map<Integer, Map<String, Ingredient>> vendorToIngredientsMap;

    private static final Tracer tracer = Tracing.getTracer();

    public FoodVendorDAO() {
        setupVendorToIngredientsMap();
    }

    public Ingredient getIngredient(int id, String ingredient) {
        Span span = tracer.spanBuilder("FoodVendor starting query...")
                .setRecordEvents(true)
                .setSampler(Samplers.alwaysSample())
                .startSpan();

        Map<String, Ingredient> vendorInventory = vendorToIngredientsMap.get(id);
        if (vendorInventory == null) {
            return null;
        }

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("Storage type",
                AttributeValue.stringAttributeValue(vendorInventory.getClass().getName()));
        span.addAnnotation("Annotation: storage type ", attributes);

        span.end();
        return vendorInventory.get(ingredient);
    }

    private void setupVendorToIngredientsMap() {
        vendorToIngredientsMap = new HashMap<>();

        HashMap<String, Ingredient> vendorOneIngredients = new HashMap<>();
        vendorOneIngredients.put(APPLE, new Ingredient(VENDOR_ONE, 3, 500));
        vendorOneIngredients.put(BANANA, new Ingredient(VENDOR_ONE, 10, 200));

        HashMap<String, Ingredient> vendorTwoIngredients = new HashMap<>();
        vendorTwoIngredients.put(APPLE, new Ingredient(VENDOR_TWO, 2, 300));

        HashMap<String, Ingredient> vendorThreeIngredients = new HashMap<>();
        vendorThreeIngredients.put(APPLE, new Ingredient(VENDOR_THREE, 3, 700));
        vendorThreeIngredients.put(BANANA, new Ingredient(VENDOR_THREE, 0, 800));

        vendorToIngredientsMap.put(VENDOR_ONE, vendorOneIngredients);
        vendorToIngredientsMap.put(VENDOR_TWO, vendorTwoIngredients);
        vendorToIngredientsMap.put(VENDOR_THREE, vendorThreeIngredients);
    }
}
