package com.example.kline2.domain;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class Kline2Test {

    @Test
    public void testGetOpenPrice(){
        BigDecimal expected = new BigDecimal(123);
        KLine2 line = new KLine2();
        line.setOpenPrice(expected);
        assertEquals(expected,line.getOpenPrice());
    }

    @Test
    public void testSetClosePrice(){
        BigDecimal expected = new BigDecimal(123);
        KLine2 line2 = new KLine2();
        line2.setClosePrice(expected);
        assertEquals(expected, line2.getClosePrice());
    }

    @Test
    public void testGetSetOpenTime() {
        BigDecimal expected = new BigDecimal(123456789L);
        KLine2 kline = new KLine2();
        kline.setOpenTime(expected);
        assertEquals(expected, kline.getOpenTime());
    }

    @Test
    public void testGetSetHighPrice() {
        BigDecimal expected = new BigDecimal(12345.67);
        KLine2 kline = new KLine2();
        kline.setHighPrice(expected);
        assertEquals(expected, kline.getHighPrice());
    }

    @Test
    public void testGetSetLowPrice() {
        BigDecimal expected = new BigDecimal(1234.67);
        KLine2 kline = new KLine2();
        kline.setLowPrice(expected);
        assertEquals(expected, kline.getLowPrice());
    }

    @Test
    public void testGetSetVolume() {
        BigDecimal expected = new BigDecimal(100.12345);
        KLine2 kline = new KLine2();
        kline.setVolume(expected);
        assertEquals(expected, kline.getVolume());
    }

    @Test
    public void testGetSetCloseTime() {
        BigDecimal expected = new BigDecimal(123456789L);
        KLine2 kline = new KLine2();
        kline.setCloseTime(expected);
        assertEquals(expected, kline.getCloseTime());
    }

    @Test
    public void testGetSetQuoteAssetVolume() {
        BigDecimal expected = new BigDecimal(12345.67);
        KLine2 kline = new KLine2();
        kline.setQuoteAssetVolume(expected);
        assertEquals(expected, kline.getQuoteAssetVolume());
    }

    @Test
    public void testGetSetNumOfTrades() {
        int expected = 1234;
        KLine2 kline = new KLine2();
        kline.setNumOfTrades(expected);
        assertEquals(expected, kline.getNumOfTrades());
    }

    @Test
    public void testGetSetTakerBuyBaseAssetVolume() {
        BigDecimal expected = new BigDecimal(50.456);
        KLine2 kline = new KLine2();
        kline.setTakerBuyBaseAssetVolume(expected);
        assertEquals(expected, kline.getTakerBuyBaseAssetVolume());
    }

    @Test
    public void testGetSetTakerBuyBaseQuoteAssetVolume() {
        BigDecimal expected = new BigDecimal(250000.78);
        KLine2 kline = new KLine2();
        kline.setTakerBuyBaseQuoteAssetVolume(expected);
        assertEquals(expected, kline.getTakerBuyBaseQuoteAssetVolume());
    }

    @Test
    public void testGetSetUnused() {
        int expected = 0;
        KLine2 kline = new KLine2();
        kline.setUnused(expected);
        assertEquals(expected, kline.getUnused());
    }

    @Test
    public void testGetSetSymbol() {
        String expected = "BTCUSDT";
        KLine2 kline = new KLine2();
        kline.setSymbol(expected);
        assertEquals(expected, kline.getSymbol());
    }
}
