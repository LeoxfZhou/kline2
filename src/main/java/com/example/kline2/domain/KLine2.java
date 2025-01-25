package com.example.kline2.domain;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class KLine2 {
    private BigDecimal openTime;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private BigDecimal volume;
    private BigDecimal closeTime;
    private BigDecimal quoteAssetVolume;
    private Integer numOfTrades;
    private BigDecimal takerBuyBaseAssetVolume;
    private BigDecimal takerBuyBaseQuoteAssetVolume;
    private Integer unused;
    private String symbol;
}
