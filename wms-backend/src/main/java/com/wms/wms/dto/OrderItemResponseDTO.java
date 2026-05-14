package com.wms.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponseDTO {
	
    private Long productId;
    private String productName;
    private int quantity;
}