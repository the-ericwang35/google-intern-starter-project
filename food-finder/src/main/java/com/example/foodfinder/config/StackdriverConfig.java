package com.example.foodfinder.config;

import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class StackdriverConfig {
    public StackdriverConfig() throws IOException {
        StackdriverTraceExporter.createAndRegister(
                StackdriverTraceConfiguration.builder()
                        .setProjectId("ejwang-intern-project")
                        .build());
    }
}
