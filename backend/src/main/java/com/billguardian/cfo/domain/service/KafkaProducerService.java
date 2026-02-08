package com.billguardian.cfo.domain.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendStatementEvent(String fileName,String content){
        log.info("Sending statement upload event for file: {}", fileName);
        kafkaTemplate.send("statement-uploads",fileName, content);
    }
}