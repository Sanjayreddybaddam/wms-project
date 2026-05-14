package com.wms.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageBinRequestDTO {
	
    private String binCode;
    private int capacity;
}
