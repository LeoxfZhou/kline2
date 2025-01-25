package com.example.kline2.service;

import com.example.kline2.domain.KLine2;
import com.example.kline2.mapper.KLine2Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KLine2ServiceTest {
    @Mock
    private KLine2Mapper mappker;

    @Mock
    private BinanceAPIService binanceAPIService;

    @InjectMocks
    private Kline2Service service;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(service, "combined_num", 5);
    }

    @Test
    public void testLoad() {
        //variables
        String symbol = "aaa";
        long startTime = 1L;
        long endTime = 21L;
        long maxInterval = 10L;

        //mock
        List<KLine2> mockResponse = new ArrayList<>();
        mockResponse.add(new KLine2(
                new BigDecimal("1"), new BigDecimal("100.0"), new BigDecimal("110.0"),
                new BigDecimal("120.0"), new BigDecimal("130.0"), new BigDecimal("140"),
                new BigDecimal("5"), new BigDecimal("6000"), 70,
                new BigDecimal("800"), new BigDecimal("90000"), 0, symbol
        ));


        lenient().when(binanceAPIService.get(anyString(), anyLong(), anyLong()))
                .thenReturn(mockResponse);

        // Test
        List<KLine2> result = service.load(symbol, startTime, endTime, maxInterval);

        // Verify
        verify(binanceAPIService, times(2)).get(eq(symbol), anyLong(), anyLong());
        verify(mappker, times(2)).insertBatch(any());
        assertEquals(2, result.size());

    }

    @Test
    void testOutputAll() {

        // Exact combined num
        List<KLine2> mockKlines = createMockKlines(10);
        when(mappker.findAllKlines()).thenReturn(mockKlines);

        List<KLine2> result = service.outputAll();

        // Assert
        assertEquals(2, result.size());

        // Less than combined num
        List<KLine2> mockKlines2 = createMockKlines(3);
        when(mappker.findAllKlines()).thenReturn(mockKlines2);

        List<KLine2> result2 = service.outputAll();

        // Assert
        assertEquals(mockKlines2, result2);
        assertEquals(3, result2.size());
    }

    @Test
    public void testCombine() {
        List<KLine2> mockKlines = createMockKlines(5);
        int startIndex = 0;

        KLine2 result = service.combine(mockKlines, startIndex);

        // assert
        assertNotNull(result);
        assertEquals(mockKlines.get(0).getOpenTime(), result.getOpenTime());
        assertEquals(mockKlines.get(0).getOpenPrice(), result.getOpenPrice());
        assertEquals(mockKlines.get(4).getClosePrice(), result.getClosePrice());
        assertEquals(mockKlines.get(4).getCloseTime(), result.getCloseTime());
    }

    private List<KLine2> createMockKlines(int count) {
        List<KLine2> klines = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            KLine2 kline = new KLine2();
            kline.setSymbol("BTCUSDT");
            kline.setOpenTime(new BigDecimal(1000 + i));
            kline.setOpenPrice(new BigDecimal("30000.00"));
            kline.setHighPrice(new BigDecimal("31000.00"));
            kline.setLowPrice(new BigDecimal("29000.00"));
            kline.setClosePrice(new BigDecimal("30500.00"));
            kline.setVolume(new BigDecimal("100.0"));
            kline.setCloseTime(new BigDecimal(2000 + i));
            kline.setQuoteAssetVolume(new BigDecimal("3000000.00"));
            kline.setNumOfTrades(1000);
            kline.setTakerBuyBaseAssetVolume(new BigDecimal("50.0"));
            kline.setTakerBuyBaseQuoteAssetVolume(new BigDecimal("1500000.00"));
            kline.setUnused(0);
            klines.add(kline);
        }
        return klines;
    }
}

