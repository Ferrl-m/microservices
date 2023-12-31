package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.OrderResponseDto;
import com.example.inventoryservice.eventhandler.EventHandler;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final EventHandler eventHandler;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream().map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @KafkaListener(
            topics = "general-topic",
            groupId = "groupId",
            containerFactory = "factory",
            topicPartitions = @TopicPartition(topic = "general-topic", partitions = {"1"})
    )
    public void consumeSkuCode(@Payload OrderResponseDto orderResponseDto) {
        List<InventoryResponse> inventoryResponses = isInStock(orderResponseDto.getSkuCode());
        boolean allProductsInStock = inventoryResponses.stream().allMatch(InventoryResponse::isInStock);
        orderResponseDto.setInStock(allProductsInStock);

        eventHandler.sendMessage("general-topic", 2, orderResponseDto);
    }
}
