package com.wms.wms.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wms.wms.dto.ApiResponse;
import com.wms.wms.dto.OrderRequestDTO;
import com.wms.wms.dto.OrderResponseDTO;
import com.wms.wms.entity.Order;
import com.wms.wms.enums.OrderStatus;
import com.wms.wms.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // OPERATOR / ADMIN → CREATE ORDER (ITEM BASED)
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @PostMapping("/place")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(
            @RequestBody OrderRequestDTO request,
            Authentication authentication) {

        String username = authentication.getName();

        Order saved = orderService.createOrder(request, username);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Order created successfully",
                        orderService.mapToDTO(saved),
                        LocalDateTime.now()
                )
        );
    }

    // ADMIN → UPDATE STATUS
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        orderService.updateOrderStatus(id, status);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Order status updated",
                        "Order " + id + " → " + status,
                        LocalDateTime.now()
                )
        );
    }

    // ADMIN → ALL ORDERS
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Orders fetched",
                        orderService.getAllOrders(),
                        LocalDateTime.now()
                )
        );
    }

    // OPERATOR → MY ORDERS
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getMyOrders(
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "My orders fetched",
                        orderService.getOrdersByUser(username),
                        LocalDateTime.now()
                )
        );
    }
}