package com.example.foodvendor.utils;

import io.opencensus.stats.Aggregation;
import io.opencensus.stats.BucketBoundaries;
import io.opencensus.stats.Measure;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewManager;

import java.util.Arrays;
import java.util.Collections;

public class StackdriverUtils {

    public static final Measure.MeasureDouble QUERY_LATENCY =
            Measure.MeasureDouble.create("query_latency",
                    "The latency per query", "ms");

    public static void setupSupplierRecorder() {
        Aggregation queryLatencyDistribution =
                Aggregation.Distribution.create(
                        BucketBoundaries.create(
                                Arrays.asList(
                                        // Query latency  buckets:
                                        // [>=0ms, >=200ms, >=400ms, >=600ms, >=800ms, >=1000ms]
                                        0.0, 200.0, 400.0, 600.0, 800.0, 1000.0)));

        View view =
                View.create(
                        View.Name.create("task_query_latency_distribution"),
                        "The distribution of the latency per query",
                        QUERY_LATENCY,
                        queryLatencyDistribution,
                        Collections.emptyList());

        ViewManager vmgr = Stats.getViewManager();
        vmgr.registerView(view);
    }
}