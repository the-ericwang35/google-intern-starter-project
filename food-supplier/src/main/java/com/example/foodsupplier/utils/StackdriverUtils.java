package com.example.foodsupplier.utils;

import io.opencensus.stats.Aggregation;
import io.opencensus.stats.BucketBoundaries;
import io.opencensus.stats.Measure;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewManager;

import java.util.Arrays;
import java.util.Collections;

public class StackdriverUtils {

    public static final Measure.MeasureDouble NUM_SUPPLIERS =
            Measure.MeasureDouble.create("num_suppliers",
                    "The number of suppliers per query", "suppliers");

    public static void setupSupplierRecorder() {
        Aggregation numSuppliersDistribution =
                Aggregation.Distribution.create(
                        BucketBoundaries.create(
                                Arrays.asList(
                                        // Suppliers in buckets: [>=0 suppliers, >=1 supplier, >=3 suppliers, >=5 suppliers]
                                        0.0, 2.0, 3.0, 5.0)));

        View view =
                View.create(
                        View.Name.create("task_suppliers_distribution"),
                        "The distribution of the suppliers per query",
                        NUM_SUPPLIERS,
                        numSuppliersDistribution,
                        Collections.emptyList());

        ViewManager vmgr = Stats.getViewManager();
        vmgr.registerView(view);
    }
}
