package com.example.foodfinder.services;

import com.example.foodfinder.utils.HttpUtils;
import io.opencensus.trace.Tracer;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GetSuppliersService {

    private static final String URI = "https://ejwang-intern-project.ue.r.appspot.com/ingredients/";

    public List<Integer> getSuppliers(Tracer tracer, String ingredient) {
        String urlWithParams = UriComponentsBuilder.fromHttpUrl(URI)
                .queryParam("ingredient", ingredient).toUriString();
        String response = HttpUtils.callUrl(
                tracer,
                urlWithParams,
                "Calling food supplier service");

        return Stream.of(response.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
