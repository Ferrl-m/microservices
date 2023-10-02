package com.isariev.orderservice.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaTopicConfig {

    @Bean
    public NewTopic inventoryTopic() {
        return TopicBuilder.name("inventory-topic").build();
    }

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name("order-topic").build();
    }
}
