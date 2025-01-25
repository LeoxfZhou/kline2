package com.example.kline2.controller;

import com.example.kline2.domain.KLine2;
import com.example.kline2.service.Kline2Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class Kline2ControllerTest {

    @Mock
    private Kline2Service service;

    @InjectMocks
    private Kline2Controller controller;

    @Autowired
    private MockMvc mvc;

    private Integer limit;

    @BeforeEach
    void setUp() {
        this.limit = 500;
        ReflectionTestUtils.setField(controller, "limit", 500);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGet() throws Exception {
        //Vriables
        String symbol = "BTCUSDT";
        Long startTime = 1697068382000L;
        Long endTime = 1697078392000L;
        long maxInterval = limit * 60000L;
        List<KLine2> mockKlines = createMockKlines();

        when(service.load(eq(symbol), eq(startTime), eq(endTime), eq(maxInterval)))
                .thenReturn(mockKlines);

        // Verify
        mvc.perform(get("/abc")
                        .param("symbol", symbol)
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("BTCUSDT"))
                .andExpect(jsonPath("$[0].openPrice").value("30000.0"));

        verify(service).load(symbol, startTime, endTime, maxInterval);
    }

    @Test
    public void testOutputFromData() throws Exception {
        // Prepare mock data
        List<KLine2> mockKlines = createMockKlines();
        when(service.outputAll()).thenReturn(mockKlines);

        // Verify
        mvc.perform(get("/aaa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("BTCUSDT"))
                .andExpect(jsonPath("$[0].openPrice").value("30000.0"))
                .andExpect(jsonPath("$[0].highPrice").value("31000.0"))
                .andExpect(jsonPath("$[0].lowPrice").value("29000.0"));

        // Verify called once
        verify(service).outputAll();
    }

    private List<KLine2> createMockKlines() {
        List<KLine2> klines = new ArrayList<>();
        KLine2 kline = new KLine2();
        kline.setSymbol("BTCUSDT");
        kline.setOpenTime(new BigDecimal("1625097600000"));
        kline.setOpenPrice(new BigDecimal("30000.00"));
        kline.setHighPrice(new BigDecimal("31000.00"));
        kline.setLowPrice(new BigDecimal("29000.00"));
        kline.setClosePrice(new BigDecimal("30500.00"));
        kline.setVolume(new BigDecimal("100.0"));
        kline.setCloseTime(new BigDecimal("1625184000000"));
        kline.setQuoteAssetVolume(new BigDecimal("3000000.00"));
        kline.setNumOfTrades(1000);
        kline.setTakerBuyBaseAssetVolume(new BigDecimal("50.0"));
        kline.setTakerBuyBaseQuoteAssetVolume(new BigDecimal("1500000.00"));
        kline.setUnused(0);
        klines.add(kline);
        return klines;
    }

}
