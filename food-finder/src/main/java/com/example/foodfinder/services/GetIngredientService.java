package com.example.foodfinder.services;

import com.example.foodfinder.models.Ingredient;
import com.example.foodfinder.utils.HttpUtils;
import com.google.gson.Gson;
import io.opentelemetry.trace.Tracer;
import org.springframework.web.util.UriComponentsBuilder;

public class GetIngredientService {

    private static final String URI = "https://food-vendor-dot-ejwang-intern-project.ue.r.appspot.com/inventory/";

    public Ingredient callSuppliers(Tracer tracer, int supplierId, String ingredient) {
        String urlWithParams = UriComponentsBuilder.fromHttpUrl(URI)
                .queryParam("id", supplierId)
                .queryParam("ingredient", ingredient).toUriString();

        String response = HttpUtils.callUrl(
                tracer,
                urlWithParams,
                String.format("Calling food vendor %d", supplierId));

        return new Gson().fromJson(response, Ingredient.class);
    }
}
