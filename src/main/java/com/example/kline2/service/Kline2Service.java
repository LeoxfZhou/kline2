package com.example.kline2.service;

import com.example.kline2.domain.KLine2;
import com.example.kline2.mapper.KLine2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Service
public class Kline2Service {
    @Autowired
    private KLine2Mapper mappker;

    @Autowired
    private BinanceAPIService binanceAPIService;

    @Value("${binance.url.combined_num}")
    private Integer combined_num;

    public List<KLine2> load(String symbol, Long startTime, Long endTime, Long maxInterval){
        List<KLine2> res = new ArrayList<>();

        for (long currentStartTime = startTime; currentStartTime < endTime; currentStartTime += maxInterval) {
            long currentEndTime = Math.min(currentStartTime + maxInterval, endTime);
            List<KLine2> klineList = binanceAPIService.get(symbol, currentStartTime, currentEndTime);
            if (!klineList.isEmpty()) {
                mappker.insertBatch(klineList);
            }
            res.addAll(klineList);
        }
        return res;
    }

    public List<KLine2> outputAll(){
        List<KLine2> result = new ArrayList<>();
        List<KLine2> output = mappker.findAllKlines();

        if (output.size() < combined_num) {
            return output;
        }

        for(int i = 0; i < output.size() -(combined_num-1) ; i+=combined_num){
            result.add(combine(output, i));
        }

        return result;
    }

    //combine to 5 min
    public KLine2 combine(List<KLine2> output, int order) {

        //variables
        KLine2 res = new KLine2();
        res.setOpenTime(output.get(order).getOpenTime());
        res.setOpenPrice(output.get(order).getOpenPrice());
        res.setHighPrice(output.get(order).getHighPrice());
        res.setLowPrice(output.get(order).getLowPrice());
        res.setSymbol(output.get(order).getSymbol());
        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal totalQuoteVolume = BigDecimal.ZERO;
        BigDecimal totalTakerBuyBaseVolume = BigDecimal.ZERO;
        BigDecimal totalTakerBuyQuoteVolume = BigDecimal.ZERO;
        int totalTrades = 0;

        // choose one from five
        for (int i = 0; i < combined_num; i++) {
            KLine2 currentKline = output.get(order + i);

            // comparison
            if (currentKline.getHighPrice().compareTo(res.getHighPrice()) > 0) {
                res.setHighPrice(currentKline.getHighPrice());
            }
            if (currentKline.getLowPrice().compareTo(res.getLowPrice()) < 0) {
                res.setLowPrice(currentKline.getLowPrice());
            }

            // other values
            totalVolume = totalVolume.add(currentKline.getVolume());
            totalQuoteVolume = totalQuoteVolume.add(currentKline.getQuoteAssetVolume());
            totalTakerBuyBaseVolume = totalTakerBuyBaseVolume.add(currentKline.getTakerBuyBaseAssetVolume());
            totalTakerBuyQuoteVolume = totalTakerBuyQuoteVolume.add(currentKline.getTakerBuyBaseQuoteAssetVolume());
            totalTrades += currentKline.getNumOfTrades();
        }

        // set last kline
        KLine2 lastKline = output.get(order + combined_num -1);
        res.setCloseTime(lastKline.getCloseTime());
        res.setClosePrice(lastKline.getClosePrice());

        res.setVolume(totalVolume);
        res.setQuoteAssetVolume(totalQuoteVolume);
        res.setTakerBuyBaseAssetVolume(totalTakerBuyBaseVolume);
        res.setTakerBuyBaseQuoteAssetVolume(totalTakerBuyQuoteVolume);
        res.setNumOfTrades(totalTrades);
        res.setUnused(0);

        return res;
    }
    
    
}
