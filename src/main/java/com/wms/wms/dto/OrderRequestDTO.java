package com.wms.wms.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private List<OrderItemDTO> items;
}
