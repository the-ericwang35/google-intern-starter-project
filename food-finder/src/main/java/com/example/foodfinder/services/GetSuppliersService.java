package com.example.foodfinder.services;

import com.example.foodfinder.utils.HttpUtils;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.propagation.TextFormat;
import io.opencensus.trace.propagation.TextFormat.Setter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.HttpURLConnection;
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
