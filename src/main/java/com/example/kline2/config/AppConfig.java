package com.example.kline2.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Aspect
public class AppConfig {

    @Bean
    public RestTemplate getTemplate(){
        return new RestTemplate();
    }

    @Around("execution(public * com.example.kline2.*.*.*(..))")
    public Object around(ProceedingJoinPoint point){

        System.out.println(point.getSignature().getName());
        Object object;
        long k = System.currentTimeMillis();
        try {
            object = point.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        System.out.println("function takes " + (System.currentTimeMillis() - k) + " ms");
        return object;
    }
}
