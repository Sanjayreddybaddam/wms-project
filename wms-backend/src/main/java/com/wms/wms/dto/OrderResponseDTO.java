package com.wms.wms.dto;

import java.util.List;

import com.wms.wms.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
	
    private Long id;
    private OrderStatus status;
    private List<OrderItemResponseDTO> items;
}
