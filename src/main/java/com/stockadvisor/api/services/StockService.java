package com.stockadvisor.api.services;

import com.stockadvisor.api.client.StockApiClient;
import com.stockadvisor.api.model.StockData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class StockService implements IStockService {

    private final StockApiClient stockApiClient;

    @Autowired
    public StockService(StockApiClient stockApiClient) {
        this.stockApiClient = stockApiClient;
    }

    @Cacheable(value = "stockQuotes", key = "#symbol")
    public Mono<StockData> getStockQuote(String symbol) {
        log.info("Fetching stock quote for symbol: {}", symbol);
        return stockApiClient.getStockQuote(symbol);
    }

    @Cacheable(value = "multipleStockQuotes", key = "#symbols.toString()")
    public Flux<StockData> getMultipleStockQuotes(List<String> symbols) {
        log.info("Fetching multiple stock quotes for symbols: {}", symbols);
        String[] symbolsArray = symbols.toArray(new String[0]);
        return stockApiClient.getMultipleStockQuotes(symbolsArray)
                .flatMapMany(data -> Flux.fromIterable(Arrays.asList(data)));
    }

    @Cacheable(value = "historicalData", key = "#symbol + '-' + #timeframe")
    public Flux<StockData> getHistoricalData(String symbol, String timeframe) {
        log.info("Fetching historical data for symbol: {} with timeframe: {}", symbol, timeframe);
        return stockApiClient.getHistoricalData(symbol, timeframe)
                .flatMapMany(data -> Flux.fromIterable(Arrays.asList(data)));
    }

    @CacheEvict(value = {"stockQuotes", "multipleStockQuotes", "historicalData"}, allEntries = true)
    @Scheduled(fixedRateString = "${stock.cache.ttl:300000}")
    public void evictAllCacheValues() {
        log.info("Evicting all stock data caches");
    }
}