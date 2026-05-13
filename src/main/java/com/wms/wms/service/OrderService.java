package com.wms.wms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wms.wms.dto.OrderItemResponseDTO;
import com.wms.wms.dto.OrderRequestDTO;
import com.wms.wms.dto.OrderResponseDTO;
import com.wms.wms.entity.*;
import com.wms.wms.enums.OrderStatus;
import com.wms.wms.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    // ✅ CREATE ORDER (FIXED)
    @Transactional
    public Order createOrder(OrderRequestDTO request, String username) {

        if (request == null || request.getProductId() == null) {
            throw new RuntimeException("Invalid order request");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getWarehouse() == null) {
            throw new RuntimeException("User is not assigned to any warehouse");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Order order = new Order();
        order.setUser(user);
        order.setWarehouse(user.getWarehouse());
        order.setStatus(OrderStatus.CREATED);

        OrderItem item = new OrderItem();
        item.setProduct(product);

        // ✅ FIX: default quantity + null safety
        item.setQuantity(
                request.getQuantity() != null ? request.getQuantity() : 1
        );

        item.setOrder(order);

        order.setItems(List.of(item));

        Order saved = orderRepository.save(order);

        return saved;
    }

    // ✅ UPDATE STATUS (FIXED STOCK FLOW)
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        validateTransition(order.getStatus(), newStatus);

        Warehouse warehouse = order.getWarehouse();

        if (warehouse == null) {
            throw new RuntimeException("Warehouse missing for order");
        }

        Long warehouseId = warehouse.getId();

        // 🔥 STOCK CHECK
        if (newStatus == OrderStatus.PICKING) {

            for (OrderItem item : order.getItems()) {

                int stock = inventoryService.getTotalStock(
                        item.getProduct().getId(),
                        warehouseId
                );

                if (stock < item.getQuantity()) {
                    throw new RuntimeException(
                            "Insufficient stock for " + item.getProduct().getName()
                    );
                }
            }
        }

        // 🔥 STOCK DEDUCTION
        if (newStatus == OrderStatus.PACKED) {

            for (OrderItem item : order.getItems()) {

                inventoryService.deductStock(
                        item.getProduct().getId(),
                        warehouseId,
                        item.getQuantity()
                );
            }
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    // ✅ GET ALL
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ✅ GET BY USER
    public List<OrderResponseDTO> getOrdersByUser(String username) {
        return orderRepository.findByUserUsername(username)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ✅ DTO MAPPER (SAFE)
    public OrderResponseDTO mapToDTO(Order order) {

        List<OrderItemResponseDTO> items = order.getItems()
                .stream()
                .map(i -> new OrderItemResponseDTO(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity()
                ))
                .toList();

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setItems(items);

        return dto;
    }

    // ✅ VALIDATION (UNCHANGED BUT SAFE)
    private void validateTransition(OrderStatus current, OrderStatus next) {

        if (current == OrderStatus.CREATED && next != OrderStatus.PICKING)
            throw new RuntimeException("Invalid transition");

        if (current == OrderStatus.PICKING && next != OrderStatus.PACKED)
            throw new RuntimeException("Invalid transition");

        if (current == OrderStatus.PACKED && next != OrderStatus.DISPATCHED)
            throw new RuntimeException("Invalid transition");

        if (current == OrderStatus.DISPATCHED && next != OrderStatus.DELIVERED)
            throw new RuntimeException("Invalid transition");
    }
}