package com.billguardian.cfo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic statementUploadTopic() {
        return TopicBuilder.name("statement-uploads")
                .partitions(3)
                .replicas(1)
                .build();
    }
}