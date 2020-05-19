package com.example.foodfinder.controller;

import com.example.foodfinder.models.Ingredient;
import com.example.foodfinder.services.GetIngredientService;
import com.example.foodfinder.services.GetSuppliersService;
import io.opentelemetry.context.Scope;
import io.opentelemetry.exporters.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpansProcessor;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FoodFinderController {

    private static final SpanProcessor spanProcessor =
            SimpleSpansProcessor.create(new LoggingSpanExporter());
    // Supplying empty string yields a default Tracer implementation, which is fine for this app.
    private static final Tracer tracer = OpenTelemetrySdk.getTracerProvider().get("");

    @GetMapping("/ingredient")
    public List<Ingredient> getIngredient(@RequestParam(value = "ingredient") String ingredient) {
        OpenTelemetrySdk.getTracerProvider().addSpanProcessor(spanProcessor);
        Span span = tracer.spanBuilder("Finding Food").startSpan();

        try (Scope ss = tracer.withSpan(span)) {
            span.setAttribute("Ingredient", ingredient);
            List<Integer> suppliers = getSuppliers(ingredient);
            return getInventory(ingredient, suppliers);
        } finally {
            spanProcessor.shutdown();
            span.end();
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
