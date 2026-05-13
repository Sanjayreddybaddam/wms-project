package com.wms.wms.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDTO {
	
	private Long productId;
    private List<OrderItemDTO> items;
    private Integer quantity = 1;
}
