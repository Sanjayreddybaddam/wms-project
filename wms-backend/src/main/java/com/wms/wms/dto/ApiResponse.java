package com.wms.wms.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private String message;
    private T data;
    private LocalDateTime timestamp;
}
