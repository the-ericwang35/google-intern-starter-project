package com.example.foodvendor.config;

import io.grpc.Context;
import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.context.ContextUtils;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.HttpTextFormat;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;
import org.springframework.lang.Nullable;
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

    // Supplying empty string yields a default Tracer implementation, which is fine for this app.
    private static final Tracer tracer = OpenTelemetrySdk.getTracerProvider().get("");
    private static final HttpTextFormat.Getter<HttpServletRequest> getter =
            new HttpTextFormat.Getter<>() {
                @Nullable
                @Override
                public String get(HttpServletRequest httpServletRequest, String s) {
                    return httpServletRequest.getHeader(s);
                }
            };


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Context extractedContext = OpenTelemetry.getPropagators().getHttpTextFormat()
                .extract(Context.current(), request, getter);
        Span serverSpan = null;

        try (Scope scope = ContextUtils.withScopedContext(extractedContext)) {
            String spanName = request.getMethod() + " " + request.getRequestURI();
            serverSpan = tracer.spanBuilder(spanName).setSpanKind(Span.Kind.SERVER).startSpan();
            serverSpan.setAttribute("http.method", "GET");
            serverSpan.setAttribute("http.scheme", "http");
            serverSpan.setAttribute("http.target", "/inventory");

            filterChain.doFilter(request, response);
        } finally {
            if (serverSpan != null) {
                serverSpan.end();
            }
        }
    }
}
