package com.billguardian.cfo.domain.service;

import com.billguardian.cfo.domain.model.Transaction;
import com.billguardian.cfo.domain.repository.TransactionRepository;
import com.billguardian.cfo.domain.strategy.BankParserStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementProcessingService {

    private final List<BankParserStrategy> strategies;
    private final TransactionRepository transactionRepository;
    private final AuditService auditService;
    private final SimpMessagingTemplate messagingTemplate;
    
    // FIX: Add this line so Spring can "inject" the AnalyticsService
    private final AnalyticsService analyticsService;

    public void process(String content) {
        String firstLine = content.split("\n")[0];

        BankParserStrategy strategy = strategies.stream()
                .filter(s -> s.supports(firstLine))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown Bank Format"));

        log.info("Auto-Detected Bank: {}", strategy.getBankName());
        
        List<Transaction> transactions = strategy.parse(content.getBytes());

        // Filter out duplicates
        List<Transaction> newTransactions = transactions.stream()
                .filter(tx -> !transactionRepository.existsByHashIdentifier(tx.getHashIdentifier()))
                .toList();

        if (newTransactions.isEmpty()) {
            log.info("No new transactions found.");
            return;
        }

        transactionRepository.saveAll(newTransactions);
        
        // --- PART 19 CACHE LOGIC ---
        // Now this will work because 'analyticsService' is defined above
        analyticsService.clearCache();
        
        auditService.runAudit();
        
        // Notify Frontend
        messagingTemplate.convertAndSend("/topic/updates", "AUDIT_COMPLETE");
    }
}