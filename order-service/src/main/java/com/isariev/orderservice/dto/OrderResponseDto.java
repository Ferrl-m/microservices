package com.isariev.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private List<String> skuCode;
    private boolean inStock;
    private List<OrderLineItemsDto> orderLineItems;

}

