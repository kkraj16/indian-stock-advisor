package com.stockadvisor.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockData {
    private String symbol;
    private String companyName;
    private BigDecimal price;
    private BigDecimal change;
    private BigDecimal changePercent;
    private Long volume;
    private BigDecimal previousClose;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private LocalDateTime lastUpdated;
}
