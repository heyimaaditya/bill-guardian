package com.billguardian.cfo.domain.service; // <--- Ensure this matches exactly

import com.billguardian.cfo.domain.model.Transaction;
import com.billguardian.cfo.domain.repository.TransactionRepository;
import com.billguardian.cfo.dto.AnalyticsSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final TransactionRepository repository;

    @Cacheable(value = "summary", key = "'dashboard'")
    public AnalyticsSummary getSummary() {
        log.info("CACHE MISS: Fetching summary from Database and calculating...");
        List<Transaction> transactions = repository.findAll();

        BigDecimal totalBurn = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> byCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        tx -> tx.getCategory().name(),
                        Collectors.mapping(Transaction::getAmount, 
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        BigDecimal potentialSavings = transactions.stream()
                .filter(tx -> tx.getAiAdvice() != null && tx.getAiAdvice().contains("SCRIPT"))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return AnalyticsSummary.builder()
                .totalMonthlyBurn(totalBurn)
                .spendingByCategory(byCategory)
                .totalPotentialSavings(potentialSavings)
                .build();
    }

    @CacheEvict(value = "summary", allEntries = true)
    public void clearCache() {
        log.info("CACHE EVICT: Clearing dashboard summary because new data was uploaded.");
    }
}