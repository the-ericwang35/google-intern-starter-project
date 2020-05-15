package com.example.foodsupplier.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceConfig {

    @Bean
    public FilterRegistrationBean<TraceFilter> tracingFilter() {
        FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TraceFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
