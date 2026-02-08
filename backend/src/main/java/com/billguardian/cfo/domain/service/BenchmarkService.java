package com.billguardian.cfo.domain.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class BenchmarkService {

    private static final Map<String, BigDecimal> FAIR_PRICES = new HashMap<>();

    static {
        FAIR_PRICES.put("netflix", new BigDecimal("15.99"));
        FAIR_PRICES.put("spotify", new BigDecimal("10.99"));
        FAIR_PRICES.put("electricity", new BigDecimal("150.00"));
        FAIR_PRICES.put("mobile", new BigDecimal("45.00"));
        FAIR_PRICES.put("internet", new BigDecimal("60.00"));
    }

    public BigDecimal getFairPrice(String merchantName) {
        String name = merchantName.toLowerCase();
        return FAIR_PRICES.entrySet().stream()
                .filter(entry -> name.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null); // Return null if we don't have a benchmark for this
    }
}