package com.isariev.orderservice.service;

import com.isariev.orderservice.dto.OrderLineItemsDto;
import com.isariev.orderservice.dto.OrderRequest;
import com.isariev.orderservice.dto.OrderResponseDto;
import com.isariev.orderservice.eventhandler.EventHandler;
import com.isariev.orderservice.model.Order;
import com.isariev.orderservice.model.OrderLineItems;
import com.isariev.orderservice.repository.OrderRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final EventHandler eventHandler;
    private final OrderRepository orderRepository;

    @Transactional
    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItemsDto> orderLineItems = orderRequest.getOrderLineItemsDtoList();
        List<String> skuCodes = orderLineItems.stream().map(OrderLineItemsDto::getSkuCode).toList();
        OrderResponseDto orderResponseDto = createOrderResponseDto(orderLineItems, skuCodes);

        eventHandler.sendMessage("general-topic", 1, orderResponseDto);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @KafkaListener(
            topics = "order-topic",
            groupId = "groupId",
            containerFactory = "factory",
            topicPartitions = @TopicPartition(topic = "general-topic", partitions = {"2"})
    )
    public void consumeInventoryResponse(@Payload OrderResponseDto orderResponseDto) {
        boolean allProductsInStock = orderResponseDto.isInStock();
        if (allProductsInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            List<OrderLineItems> orderLineItems = orderResponseDto.getOrderLineItems()
                    .stream().map(this::mapToEntity)
                    .toList();

            order.setOrderLineItemsList(orderLineItems);
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again latter");
        }
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

    private OrderResponseDto createOrderResponseDto(List<OrderLineItemsDto> orderLineItems, List<String> skuCodes) {
        return OrderResponseDto.builder()
                .id(1)
                .skuCode(skuCodes)
                .inStock(false)
                .orderLineItems(orderLineItems)
                .build();
    }
}
