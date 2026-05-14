package com.wms.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageBinResponseDTO {

    private Long id;
    private String binCode;
    private int capacity;
    private int currentQuantity;
    private Long warehouseId;
}