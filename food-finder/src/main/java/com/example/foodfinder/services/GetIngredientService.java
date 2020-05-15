package com.example.foodfinder.services;

import com.example.foodfinder.models.Ingredient;
import com.example.foodfinder.utils.SpanUtils;
import com.google.gson.Gson;
import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.propagation.TextFormat;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetIngredientService {

    private static final String URI = "https://food-vendor-dot-ejwang-intern-project.ue.r.appspot.com/inventory/";
    private static final TextFormat textFormat = Tracing.getPropagationComponent().getB3Format();
    private static final TextFormat.Setter<HttpURLConnection> setter = new TextFormat.Setter<>() {
        public void put(HttpURLConnection carrier, String key, String value) {
            carrier.setRequestProperty(key, value);
        }
    };

    public Ingredient callSuppliers(Tracer tracer, int supplierId, String ingredient) {
        Span span = SpanUtils.buildSpan(tracer,
                String.format("Calling food supplier service %d", supplierId))
                .startSpan();
        Ingredient respIngredient = null;

        try (Scope s = tracer.withSpan(span)) {
            String UrlWithParams = UriComponentsBuilder.fromHttpUrl(URI)
                    .queryParam("id", supplierId)
                    .queryParam("ingredient", ingredient).toUriString();

            HttpURLConnection conn = (HttpURLConnection) new URL(UrlWithParams).openConnection();
            textFormat.inject(span.getContext(), conn, setter);
            conn.setRequestMethod(HttpMethod.GET.name());

            // Read/parse response.
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = rd.readLine();
            respIngredient = new Gson().fromJson(line, Ingredient.class);

            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respIngredient;

//        // TODO: Move this construction to its own helper
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URI)
//                .queryParam("id", supplierId)
//                .queryParam("ingredient", ingredient);
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<Ingredient> response = restTemplate.exchange(
//                builder.toUriString(),
//                HttpMethod.GET,
//                entity,
//                Ingredient.class);
//        return response.getBody();
    }
}
