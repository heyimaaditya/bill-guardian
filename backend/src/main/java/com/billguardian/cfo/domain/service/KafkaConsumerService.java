package com.billguardian.cfo.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final StatementProcessingService processingService;

    @KafkaListener(topics = "statement-uploads", groupId = "bg-group")
    public void consumeStatement(String content) {
        log.info("KAFKA SUCCESS: Consumer received the statement data!");
        
        // Call the processing service to parse and save
        processingService.process(content);
    }
}