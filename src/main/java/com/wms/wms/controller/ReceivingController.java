package com.wms.wms.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wms.wms.dto.ApiResponse;
import com.wms.wms.dto.ReceivingRequest;
import com.wms.wms.dto.ReceivingResponse;
import com.wms.wms.service.ReceivingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/receiving")
@RequiredArgsConstructor
public class ReceivingController {

    private final ReceivingService receivingService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReceivingResponse>> receiveShipment(
            @RequestBody ReceivingRequest request) {

        ReceivingResponse response = receivingService.receiveShipment(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        "Shipment received successfully",
                        response,
                        LocalDateTime.now()
                ));
    }
}