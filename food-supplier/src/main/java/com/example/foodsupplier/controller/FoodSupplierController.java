package com.example.foodsupplier.controller;

import com.example.foodsupplier.services.FoodSupplierService;
import io.opencensus.trace.AttributeValue;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.samplers.Samplers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FoodSupplierController {

    private final FoodSupplierService service = new FoodSupplierService();
    private static final Tracer tracer = Tracing.getTracer();

    @GetMapping("/ingredients")
    public String getVendors(@RequestParam(value = "ingredient") String ingredient) {

        Span span = tracer.spanBuilder("FoodSupplier querying vendors")
                .setRecordEvents(true)
                .setSampler(Samplers.alwaysSample())
                .startSpan();

        String vendors = service.getVendors(ingredient);

        Map<String, AttributeValue> attributes = new HashMap<>();
        attributes.put("Number of suppliers",
                AttributeValue.longAttributeValue(vendors.split(",").length));
        span.addAnnotation("Finished querying suppliers: ", attributes);

        span.end();
        return vendors;
    }

    @GetMapping("/")
    public String home() {
        return "Success";
    }
}
