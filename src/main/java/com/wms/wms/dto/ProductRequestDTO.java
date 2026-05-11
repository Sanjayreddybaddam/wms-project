package com.wms.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequestDTO {
    private String name;
    private String sku;
}
