package com.example.kline2.service;

import com.example.kline2.domain.KLine2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class BinanceAPIService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${binance.url_template}")
    private String urlTemplate;

    @Value("${binance.url.limit}")
    private Integer limit;

    public List<KLine2> get(String symbol, Long currentStartTime, Long currentEndTime){

        String url = String.format(urlTemplate, symbol, currentStartTime, currentEndTime, limit);

        System.out.println("url is " + url);

        ResponseEntity<String[][]> response
                = restTemplate.getForEntity(url, String[][].class);
        String[][] dataInsert = response.getBody();

        if (dataInsert == null) {
            throw new RuntimeException("Failed to get data from Binance API");
        }

        return convert(symbol,dataInsert);
    }

    //input api
    public List<KLine2> convert(String symbol, String[][] result){
        List<KLine2> res = new ArrayList<>() ;
        for (String[] strings : result) {
            KLine2 kline = new KLine2();
            kline.setOpenTime(new BigDecimal(strings[0]));
            kline.setOpenPrice(new BigDecimal(strings[1]));
            kline.setHighPrice(new BigDecimal(strings[2]));
            kline.setLowPrice(new BigDecimal(strings[3]));
            kline.setClosePrice(new BigDecimal(strings[4]));
            kline.setVolume(new BigDecimal(strings[5]));
            kline.setCloseTime(new BigDecimal(strings[6]));
            kline.setQuoteAssetVolume(new BigDecimal(strings[7]));
            kline.setNumOfTrades(Integer.valueOf(strings[8]));
            kline.setTakerBuyBaseAssetVolume(new BigDecimal(strings[9]));
            kline.setTakerBuyBaseQuoteAssetVolume(new BigDecimal(strings[10]));
            kline.setUnused(Integer.valueOf(strings[11]));
            kline.setSymbol(symbol);
            res.add(kline);
        }

        return res;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public Integer getLimit() {
        return limit;
    }


}

