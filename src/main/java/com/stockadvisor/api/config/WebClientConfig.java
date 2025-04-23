package com.stockadvisor.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebClientConfig.class);

    @Value("${stock.api.base-url}")
    private String stockApiBaseUrl;

    @Value("${stock.api.key}")
    private String stockApiKey;

    @Bean
    public WebClient stockApiClient() {
        return WebClient.builder()
                .baseUrl(stockApiBaseUrl)
                .filter(logRequest())
                .filter(logResponse())
                .defaultHeader("X-API-KEY", stockApiKey)
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("Response status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}