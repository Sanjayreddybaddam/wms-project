package com.wms.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ReceivingResponse {

    private Long inventoryItemId;
    private String productName;
    private String productSku;
    private int quantityReceived;
    private String assignedBinCode;   // e.g. "A-01-03"
    private String warehouseName;
    private String supplierReference;
    private String message;
}