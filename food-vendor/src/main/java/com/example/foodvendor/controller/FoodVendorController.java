package com.example.foodvendor.controller;

import com.example.foodvendor.models.Ingredient;
import com.example.foodvendor.services.FoodVendorService;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.samplers.Samplers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodVendorController {

    // TODO: Inject this.
    FoodVendorService foodVendorService = new FoodVendorService();
    private static final Tracer tracer = Tracing.getTracer();

    @GetMapping("/inventory")
    public Ingredient getInventory(@RequestParam(value = "id") int id,
                                   @RequestParam(value = "ingredient") String ingredient) {

        Span span = tracer.spanBuilder("FoodVendor querying inventory")
                .setRecordEvents(true)
                .setSampler(Samplers.alwaysSample())
                .startSpan();

        Ingredient queriedIngredient = foodVendorService.getIngredient(id, ingredient);
        span.end();
        return queriedIngredient;
    }

    @GetMapping("/")
    public String home() {
        return "Success";
    }
}
