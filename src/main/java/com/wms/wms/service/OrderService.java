package com.wms.wms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wms.wms.dto.OrderItemDTO;
import com.wms.wms.dto.OrderItemResponseDTO;
import com.wms.wms.dto.OrderRequestDTO;
import com.wms.wms.dto.OrderResponseDTO;
import com.wms.wms.entity.Order;
import com.wms.wms.entity.OrderItem;
import com.wms.wms.entity.Product;
import com.wms.wms.enums.OrderStatus;
import com.wms.wms.repository.OrderRepository;
import com.wms.wms.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final InventoryService inventoryService;
	
    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderRequestDTO request) {

    	Order order = new Order();

        List<OrderItem> items = new ArrayList<>();


        for (OrderItemDTO dto : request.getItems()) {

            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());

            item.setOrder(order);

            items.add(item);
        }


        order.setItems(items);
        order.setStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        validateTransition(order.getStatus(), newStatus);

        Long warehouseId = order.getWarehouse().getId(); // OR order.getWarehouseId()

        // STEP 1: STOCK CHECK
        if (newStatus == OrderStatus.PICKING) {

            for (OrderItem item : order.getItems()) {

                int stock = inventoryService.getTotalStock(
                        item.getProduct().getId(),
                        warehouseId
                );

                if (stock < item.getQuantity()) {
                    throw new RuntimeException(
                            "Insufficient stock for product: " + item.getProduct().getName()
                    );
                }
            }
        }

        // STEP 2: DEDUCT STOCK
        if (newStatus == OrderStatus.PACKED) {

            for (OrderItem item : order.getItems()) {

                inventoryService.deductStock(
                        item.getProduct().getId(),
                        warehouseId,   // 🔥 IMPORTANT FIX
                        item.getQuantity()
                );
            }
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    // ✅ Strict workflow enforcement
    private void validateTransition(OrderStatus current, OrderStatus next) {

        switch (current) {

            case PENDING:
                if (next != OrderStatus.PICKING) {
                    throw new RuntimeException("PENDING can only go to PICKING");
                }
                break;

            case PICKING:
                if (next != OrderStatus.PACKED) {
                    throw new RuntimeException("PICKING can only go to PACKED");
                }
                break;

            case PACKED:
                if (next != OrderStatus.SHIPPED) {
                    throw new RuntimeException("PACKED can only go to SHIPPED");
                }
                break;

            case SHIPPED:
                throw new RuntimeException("Cannot change status after SHIPPED");
        }
    }
    
    public OrderResponseDTO mapToDTO(Order order) {

        List<OrderItemResponseDTO> items = order.getItems().stream()
                .map(i -> new OrderItemResponseDTO(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity()
                ))
                .toList();

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus().name());
        dto.setItems(items);

        return dto;
    }

    public List<OrderResponseDTO> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
    
}