package com.example.foodfinder.controller;

import com.example.foodfinder.models.Form;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FoodFinderController {

    private static final SpanProcessor spanProcessor =
            SimpleSpansProcessor.create(new LoggingSpanExporter());
    // Supplying empty string yields a default Tracer implementation, which is fine for this app.
    private static final Tracer tracer = OpenTelemetrySdk.getTracerProvider().get("");

    @GetMapping("/ingredient")
    public String getIngredient(@ModelAttribute Form form, Model model) {
        OpenTelemetrySdk.getTracerProvider().addSpanProcessor(spanProcessor);
        Span span = tracer.spanBuilder("Food-finer running...").startSpan();

        String ingredient = form.getIngredient();
        System.out.println("HERE: " + ingredient);

        try (Scope ss = tracer.withSpan(span)) {
            span.setAttribute("Ingredient", ingredient);
            List<Integer> suppliers = getSuppliers(ingredient);
            List<Ingredient> ingredients = getInventory(ingredient, suppliers);
            model.addAttribute("ingredients", ingredients);
            return "ingredients.html";

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
            if (ingredientInfo != null) {
                ingredients.add(ingredientInfo);
            }
        }
        return ingredients;
    }

    @GetMapping("/errorfour")
    public ResponseEntity<HttpStatus> errorFour() {
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null);
    }

    @GetMapping("/errorfive")
    public ResponseEntity<HttpStatus> errorFive() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
