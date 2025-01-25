package com.example.kline2.service;

import com.example.kline2.domain.KLine2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.http.ResponseEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BinanceAPIServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BinanceAPIService service;

    //@Value("${binance.url_template}")
    private String urlTemplate;

    //@Value("${binance.url.limit}")
    private Integer limit;


    @BeforeEach
    public void setup() {
        this.urlTemplate = "https://www.binance.com/api/v3/klines?symbol=%s&interval=1m&startTime=%s&endTime=%s&limit=%s";
        this.limit = 500;

        ReflectionTestUtils.setField(service, "urlTemplate", urlTemplate);
        ReflectionTestUtils.setField(service, "limit", limit);

    }

    @Test
    public void testGet(){
        long start = 1L;
        long end = 2L;
        String symbol = "a";

        String expectedUrl = String.format(service.getUrlTemplate(), symbol, start, end, service.getLimit());

        // Mock response
        String[][] dataInsertMock = new String[][]{
                {"1","2","3","4","5","6","7","8","9","10","11","12"}
        };
        ResponseEntity<String[][]> mockedResponse = ResponseEntity.ok(dataInsertMock);
        when(restTemplate.getForEntity(expectedUrl, String[][].class))
                .thenReturn(mockedResponse);

        // Test
        List<KLine2> result = service.get(symbol, start, end);
        verify(restTemplate).getForEntity(expectedUrl, String[][].class);
        assertEquals(1, result.size());

        KLine2 firstKline = result.get(0);
        assertEquals(new BigDecimal("1"), firstKline.getOpenTime());
        assertEquals(new BigDecimal("2"), firstKline.getOpenPrice());
        assertEquals(new BigDecimal("3"), firstKline.getHighPrice());
        assertEquals(symbol, firstKline.getSymbol());


    }

    @Test
    public void testConvert(){
        String symbol = "aaa";
        String[][] mockData = new String[][]{
                {"111111111", "1111.11", "1111.111", "1111.11", "111111.11", "1111111.11", "1111111111", "1111111.111", "1234", "111111", "1111.11", "0"}};

        List<KLine2> result = service.convert(symbol, mockData);

        assertNotNull(result);
        assertEquals(1, result.size());

        KLine2 kline = result.get(0);
        assertEquals(new BigDecimal("111111111"), kline.getOpenTime());
        assertEquals(new BigDecimal("1111.11"), kline.getOpenPrice());
        assertEquals(new BigDecimal("1111.111"), kline.getHighPrice());
        assertEquals(new BigDecimal("1111.11"), kline.getLowPrice());
        assertEquals(new BigDecimal("111111.11"), kline.getClosePrice());
        assertEquals(new BigDecimal("1111111.11"), kline.getVolume());
        assertEquals(new BigDecimal("1111111111"), kline.getCloseTime());
        assertEquals(new BigDecimal("1111111.111"), kline.getQuoteAssetVolume());
        assertEquals(1234, kline.getNumOfTrades());
        assertEquals(new BigDecimal("111111"), kline.getTakerBuyBaseAssetVolume());
        assertEquals(new BigDecimal("1111.11"), kline.getTakerBuyBaseQuoteAssetVolume());
        assertEquals(0, kline.getUnused());
        assertEquals("aaa", kline.getSymbol());

        }

    @Test
    void testGetUrlTemplate() {
        assertEquals(urlTemplate, service.getUrlTemplate());
    }

    @Test
    void testGetLimit() {
        assertEquals(limit, service.getLimit());
    }

}
