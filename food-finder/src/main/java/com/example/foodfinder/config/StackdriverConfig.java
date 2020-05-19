package com.example.foodfinder.config;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class StackdriverConfig {
    public StackdriverConfig() throws IOException {
        // TODO: Can't find Stackdriver exporter for OpenTelemetry Java.
        //  Using LoggingSpanExporter to test locally for now.
    }
}
