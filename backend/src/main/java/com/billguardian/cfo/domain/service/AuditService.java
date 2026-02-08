package com.billguardian.cfo.domain.service;

import com.billguardian.cfo.domain.model.Transaction;
import com.billguardian.cfo.domain.model.TransactionCategory;
import com.billguardian.cfo.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class AuditService {

    private final TransactionRepository repository;
    private final CfoAiService aiService;
    private final BenchmarkService benchmarkService; 

    public void runAudit() {
    log.info("Starting audit process with AI Insights...");
    List<Transaction> allTransactions = repository.findAll();
    
    Map<String, List<Transaction>> grouped = allTransactions.stream()
            .collect(Collectors.groupingBy(Transaction::getMerchantName));

    grouped.forEach((merchant, txList) -> {
        TransactionCategory detectedCategory = determineCategory(merchant);
        
        txList.forEach(tx -> {
            tx.setCategory(detectedCategory);
            
            // NEW: Get AI Advice for this specific transaction
            String advice = aiService.getAdvice(tx);
            tx.setAiAdvice(advice);
            tx.setCommunityAverage(benchmarkService.getFairPrice(merchant));
        });
        
        repository.saveAll(txList);
    });
    log.info("Audit and AI Analysis completed!");
}
    private TransactionCategory determineCategory(String merchantName){
        String name= merchantName.toLowerCase();
        if(name.contains("netflix") || name.contains("spotify")){
            return TransactionCategory.SUBSCRIPTION;
        } else if(name.contains("electricity") || name.contains("water")|| name.contains("gas")||name.contains("bill")){
            return TransactionCategory.UTILITY;
        } else if(name.contains("cafe") || name.contains("restaurant")){
            return TransactionCategory.LIFESTYLE;
        } else if(name.contains("rent")){
            return TransactionCategory.HOUSING;
        } else if(name.contains("uber") || name.contains("rickshaw")){
            return TransactionCategory.TRANSPORT;
        } else {
            return TransactionCategory.UNCATEGORIZED;
        }
    }
}