package com.isariev.orderservice.eventhandler;

import com.isariev.orderservice.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventHandler {

    private final KafkaTemplate<String, OrderResponseDto> kafkaTemplate;

    public void sendMessage(String topic, Integer partition, OrderResponseDto orderResponseDto) {
        kafkaTemplate.send(topic, partition, String.valueOf(orderResponseDto.getId()), orderResponseDto);
    }
}
