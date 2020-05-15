package com.example.foodfinder.services;

import com.example.foodfinder.utils.SpanUtils;
import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.propagation.TextFormat;
import io.opencensus.trace.propagation.TextFormat.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetSuppliersService {

    private static final String URI = "https://ejwang-intern-project.ue.r.appspot.com/ingredients/";
    private static final TextFormat textFormat = Tracing.getPropagationComponent().getB3Format();
    private static final Setter<HttpURLConnection> setter = new Setter<>() {
        public void put(HttpURLConnection carrier, String key, String value) {
            carrier.setRequestProperty(key, value);
        }
    };

    public List<Integer> getSuppliers(Tracer tracer, String ingredient) {
        Span span = SpanUtils.buildSpan(tracer, "Calling food supplier service").startSpan();
        List<Integer> suppliers = Collections.emptyList();

        try (Scope s = tracer.withSpan(span)) {
            String UrlWithParams = UriComponentsBuilder.fromHttpUrl(URI)
                    .queryParam("ingredient", ingredient).toUriString();

            HttpURLConnection conn = (HttpURLConnection) new URL(UrlWithParams).openConnection();
            textFormat.inject(span.getContext(), conn, setter);
            conn.setRequestMethod(HttpMethod.GET.name());

            // Read/parse response.
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = rd.readLine();

            suppliers = Stream.of(line.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        span.end();
        return suppliers;
    }
}
