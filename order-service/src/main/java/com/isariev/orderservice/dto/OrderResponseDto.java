package com.isariev.orderservice.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private int id;
    private List<String> skuCode;
    private boolean inStock;
    private List<OrderLineItemsDto> orderLineItems;

}

