package com.example.foodvendor.data;

import com.example.foodvendor.models.Ingredient;
import io.opencensus.stats.Stats;
import io.opencensus.stats.StatsRecorder;
import io.opentelemetry.context.Scope;
import io.opentelemetry.exporters.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.SimpleSpansProcessor;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;

import java.util.HashMap;
import java.util.Map;

import static com.example.foodvendor.utils.StackdriverUtils.QUERY_LATENCY;

public class FoodVendorDAO {

    private static final String APPLE = "apple";
    private static final String BANANA = "banana";
    private static final String HAIRCUT = "haircut";
    private static final int VENDOR_ONE = 1;
    private static final int VENDOR_TWO = 2;
    private static final int VENDOR_THREE = 3;

    private Map<Integer, Map<String, Ingredient>> vendorToIngredientsMap;

    private static final SpanProcessor spanProcessor =
            SimpleSpansProcessor.create(new LoggingSpanExporter());
    private static final Tracer tracer = OpenTelemetrySdk.getTracerProvider().get("");
    private static final StatsRecorder statsRecorder = Stats.getStatsRecorder();

    public FoodVendorDAO() {
        setupVendorToIngredientsMap();
    }

    public Ingredient getIngredient(int id, String ingredient) throws InterruptedException {
        OpenTelemetrySdk.getTracerProvider().addSpanProcessor(spanProcessor);
        Span span = tracer.spanBuilder("FoodVendor starting query...").startSpan();

        try (Scope scope = tracer.withSpan(span)) {
            Map<String, Ingredient> vendorInventory = vendorToIngredientsMap.get(id);

            // Sleep for a random time to simulate querying.
            long sleepTime = (long)(Math.random() * 1000);
            Thread.sleep(sleepTime);

            statsRecorder.newMeasureMap().put(QUERY_LATENCY, sleepTime).record();

            if (vendorInventory == null) {
                return null;
            }
            span.setAttribute("Storage Type", vendorInventory.getClass().getName());
            return vendorInventory.get(ingredient);
        } finally {
            span.end();
            spanProcessor.shutdown();
        }
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
        vendorThreeIngredients.put(HAIRCUT, new Ingredient(VENDOR_THREE, 1, 3500));

        vendorToIngredientsMap.put(VENDOR_ONE, vendorOneIngredients);
        vendorToIngredientsMap.put(VENDOR_TWO, vendorTwoIngredients);
        vendorToIngredientsMap.put(VENDOR_THREE, vendorThreeIngredients);
    }
}
