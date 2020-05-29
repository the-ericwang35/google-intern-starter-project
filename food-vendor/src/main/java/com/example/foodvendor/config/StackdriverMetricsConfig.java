package com.example.foodvendor.config;

import io.opencensus.exporter.stats.stackdriver.StackdriverStatsExporter;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class StackdriverMetricsConfig {
    public StackdriverMetricsConfig() throws IOException {
        StackdriverStatsExporter.createAndRegister();
    }
}