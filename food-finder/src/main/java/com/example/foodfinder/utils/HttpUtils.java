package com.example.foodfinder.utils;

import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.propagation.TextFormat;
import org.springframework.http.HttpMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    private static final TextFormat textFormat = Tracing.getPropagationComponent().getB3Format();
    private static final TextFormat.Setter<HttpURLConnection> setter = new TextFormat.Setter<>() {
        public void put(HttpURLConnection carrier, String key, String value) {
            carrier.setRequestProperty(key, value);
        }
    };

    public static String callUrl(Tracer tracer, String url, String spanName) {
        Span span = SpanUtils.buildSpan(tracer, spanName).startSpan();
        String line = "";

        try (Scope s = tracer.withSpan(span)) {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            textFormat.inject(span.getContext(), conn, setter);
            conn.setRequestMethod(HttpMethod.GET.name());

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            line = rd.readLine();
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        span.end();
        return line;
    }
}
