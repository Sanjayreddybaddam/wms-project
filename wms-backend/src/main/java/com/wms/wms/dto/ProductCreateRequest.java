package com.wms.wms.dto;

import lombok.Data;

@Data
public class ProductCreateRequest {

    private String name;
    private String sku;
    private double price;

    private Integer stock;      // initial stock
    private Long storageBinId;  // where stock goes
}
