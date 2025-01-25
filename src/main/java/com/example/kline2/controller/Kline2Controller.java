package com.example.kline2.controller;

import com.example.kline2.domain.KLine2;
import com.example.kline2.mapper.KLine2Mapper;
import com.example.kline2.service.Kline2Service;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.javassist.compiler.ast.Symbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Kline2Controller {

    @Autowired
    private Kline2Service service;

    @Value("${binance.url.limit}")
    private Integer limit;

    @GetMapping("/abc")
    public List<KLine2> get(@RequestParam(name = "startTime") Long startTime, @RequestParam(name = "endTime") Long endTime, @RequestParam(name = "symbol") String symbol) {

        long maxInterval = limit * 60000L;
        return service.load(symbol, startTime, endTime, maxInterval);
    }

    @GetMapping("/aaa")
    public List<KLine2> outputFromData(){
        return service.outputAll();
    }

}
