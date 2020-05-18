package com.example.foodfinder.services;

import com.example.foodfinder.models.Ingredient;
import com.example.foodfinder.utils.HttpUtils;
import com.google.gson.Gson;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.propagation.TextFormat;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.HttpURLConnection;

public class GetIngredientService {

    private static final String URI = "https://food-vendor-dot-ejwang-intern-project.ue.r.appspot.com/inventory/";
    private static final TextFormat textFormat = Tracing.getPropagationComponent().getB3Format();
    private static final TextFormat.Setter<HttpURLConnection> setter = new TextFormat.Setter<>() {
        public void put(HttpURLConnection carrier, String key, String value) {
            carrier.setRequestProperty(key, value);
        }
    };

    public Ingredient callSuppliers(Tracer tracer, int supplierId, String ingredient) {
        String urlWithParams = UriComponentsBuilder.fromHttpUrl(URI)
                .queryParam("id", supplierId)
                .queryParam("ingredient", ingredient).toUriString();

        String response = HttpUtils.callUrl(
                tracer,
                urlWithParams,
                String.format("Calling food supplier service %d", supplierId));

        return new Gson().fromJson(response, Ingredient.class);
    }
}
