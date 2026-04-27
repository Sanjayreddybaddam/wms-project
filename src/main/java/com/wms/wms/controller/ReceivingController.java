package com.wms.wms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wms.wms.dto.ReceivingRequest;
import com.wms.wms.dto.ReceivingResponse;
import com.wms.wms.service.ReceivingService;

/**
 * REST controller for inbound shipment receiving.
 *
 * Endpoint: POST /api/receiving
 *
 * Example request body:
 * {
 *   "productId": 1,
 *   "warehouseId": 1,
 *   "quantity": 50,
 *   "supplierReference": "PO-2024-00891"
 * }
 */
@RestController
@RequestMapping("/api/receiving")
public class ReceivingController {

    private static final Logger logger = LoggerFactory.getLogger(ReceivingController.class);

    @Autowired
    private ReceivingService receivingService;

    @PostMapping
    public ResponseEntity<ReceivingResponse> receiveShipment(@RequestBody ReceivingRequest request) {
        logger.info("Received shipment request: productId={}, warehouseId={}, qty={}",
            request.getProductId(), request.getWarehouseId(), request.getQuantity(),request.getSupplierReference());

        ReceivingResponse response = receivingService.receiveShipment(request);

        logger.info("Shipment processed successfully. Assigned bin: {}", response.getAssignedBinCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}