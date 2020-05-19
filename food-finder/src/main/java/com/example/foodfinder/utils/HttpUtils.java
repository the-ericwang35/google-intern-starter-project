package com.example.foodfinder.utils;

import io.grpc.Context;
import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.HttpTextFormat;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;
import org.springframework.http.HttpMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpUtils {
    private static final HttpTextFormat.Setter<HttpURLConnection> setter =
            URLConnection::setRequestProperty;

    public static String callUrl(Tracer tracer, String url, String spanName) {
        Span span = tracer.spanBuilder(spanName).setSpanKind(Span.Kind.CLIENT).startSpan();
        String line = "";

        try (Scope s = tracer.withSpan(span)) {
            span.setAttribute("http.method", "GET");
            span.setAttribute("http.url", url);

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            OpenTelemetry.getPropagators().getHttpTextFormat().inject(Context.current(), conn, setter);
            conn.setRequestMethod(HttpMethod.GET.name());

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            line = rd.readLine();
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            span.end();
        }

        return line;
    }
}
