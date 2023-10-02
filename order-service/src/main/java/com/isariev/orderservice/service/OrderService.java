package com.isariev.orderservice.service;

import com.isariev.orderservice.dto.OrderLineItemsDto;
import com.isariev.orderservice.dto.OrderRequest;
import com.isariev.orderservice.dto.OrderResponseDto;
import com.isariev.orderservice.model.Order;
import com.isariev.orderservice.model.OrderLineItems;
import com.isariev.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final KafkaTemplate<String, OrderResponseDto> kafkaTemplate;
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItemsDto> orderLineItems = orderRequest.getOrderLineItemsDtoList();
        List<String> skuCodes = orderLineItems.stream().map(OrderLineItemsDto::getSkuCode).toList();

        kafkaTemplate.send("order-topic", new OrderResponseDto(skuCodes, false, orderLineItems));
    }

    @KafkaListener(topics = "inventory-topic", groupId = "groupID")
    public void consumeInventoryResponse(OrderResponseDto orderResponseDto) {
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
}
