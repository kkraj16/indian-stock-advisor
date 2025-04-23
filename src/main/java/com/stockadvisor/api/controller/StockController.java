package com.stockadvisor.api.controller;

import com.stockadvisor.api.model.StockData;
import com.stockadvisor.api.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@Slf4j
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping(value = "/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<StockData> getStockQuote(@PathVariable String symbol) {
        log.info("Received request for stock quote: {}", symbol);
        return stockService.getStockQuote(symbol);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<StockData> getMultipleStockQuotes(@RequestParam String symbols) {
        List<String> symbolsList = Arrays.asList(symbols.split(","));
        log.info("Received request for multiple stock quotes: {}", symbolsList);
        return stockService.getMultipleStockQuotes(symbolsList);
    }

    @GetMapping(value = "/{symbol}/historical", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<StockData> getHistoricalData(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "1d") String timeframe) {
        log.info("Received request for historical data: {} with timeframe: {}", symbol, timeframe);
        return stockService.getHistoricalData(symbol, timeframe);
    }
}
