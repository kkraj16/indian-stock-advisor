package com.stockadvisor.api.client;

import com.stockadvisor.api.model.StockData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class StockApiClient {

    private final WebClient webClient;

    @Autowired
    public StockApiClient(WebClient stockWebClient) {  // Changed parameter name to match the bean name
        this.webClient = stockWebClient;
    }

    public Mono<StockData> getStockQuote(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/quote")
                        .queryParam("symbol", symbol)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StockData.class)
                .timeout(Duration.ofSeconds(10))
                .doOnError(WebClientResponseException.class, e ->
                        log.error("Error calling stock API: {} - {}", e.getStatusCode(), e.getMessage())
                );
    }

    // Rest of the methods remain the same
    public Mono<StockData[]> getMultipleStockQuotes(String... symbols) {
        String symbolsString = String.join(",", symbols);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/quotes")
                        .queryParam("symbols", symbolsString)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StockData[].class)
                .timeout(Duration.ofSeconds(15))
                .doOnError(WebClientResponseException.class, e ->
                        log.error("Error calling stock API for multiple quotes: {} - {}", e.getStatusCode(), e.getMessage())
                );
    }

    public Mono<StockData[]> getHistoricalData(String symbol, String timeframe) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/historical")
                        .queryParam("symbol", symbol)
                        .queryParam("timeframe", timeframe)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StockData[].class)
                .timeout(Duration.ofSeconds(15))
                .doOnError(WebClientResponseException.class, e ->
                        log.error("Error calling stock API for historical data: {} - {}", e.getStatusCode(), e.getMessage())
                );
    }
}