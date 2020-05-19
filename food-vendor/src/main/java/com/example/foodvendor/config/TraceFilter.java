package com.example.foodvendor.config;

import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.SpanBuilder;
import io.opencensus.trace.SpanContext;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.propagation.SpanContextParseException;
import io.opencensus.trace.propagation.TextFormat;
import io.opencensus.trace.samplers.Samplers;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is used to receive parent tracers from other services
 * (so that trace contexts are propagated between each service).
 */

public class TraceFilter extends OncePerRequestFilter {

    private static final Tracer tracer = Tracing.getTracer();
    private static final TextFormat textFormat = Tracing.getPropagationComponent().getB3Format();
    private static final TextFormat.Getter<HttpServletRequest> getter = new TextFormat.Getter<>() {
        @Override
        public String get(HttpServletRequest httpRequest, String s) {
            return httpRequest.getHeader(s);
        }
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        SpanContext spanContext;
        SpanBuilder spanBuilder;

        String spanName = request.getMethod() + " " + request.getRequestURI();

        try {
            spanContext = textFormat.extract(request, getter);
            spanBuilder = tracer.spanBuilderWithRemoteParent(spanName, spanContext);
        } catch (SpanContextParseException e) {
            // If we're not given a parent span from the request
            // create the span without a parent.
            System.out.println("Parent span was not extracted.");
            spanBuilder = tracer.spanBuilder(spanName);
        }

        Span span = spanBuilder
                .setRecordEvents(true)
                .setSampler(Samplers.alwaysSample())
                .startSpan();

        try (Scope s = tracer.withSpan(span)) {
            filterChain.doFilter(request, response);
        }

        span.end();
    }
}
