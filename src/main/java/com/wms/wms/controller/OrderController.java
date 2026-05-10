package com.wms.wms.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wms.wms.dto.ApiResponse;
import com.wms.wms.dto.OrderRequestDTO;
import com.wms.wms.entity.Order;
import com.wms.wms.enums.OrderStatus;
import com.wms.wms.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody OrderRequestDTO order) {

        Order savedOrder = orderService.createOrder(order);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Order created successfully",
                        savedOrder,
                        LocalDateTime.now()
                )
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        orderService.updateOrderStatus(id, status);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Order status updated successfully",
                        "Order ID: " + id + " → " + status,
                        LocalDateTime.now()
                )
        );
    }
}