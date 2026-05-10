package com.wms.wms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wms.wms.dto.OrderItemDTO;
import com.wms.wms.dto.OrderRequestDTO;
import com.wms.wms.entity.InventoryItem;
import com.wms.wms.entity.Order;
import com.wms.wms.entity.OrderItem;
import com.wms.wms.entity.Product;
import com.wms.wms.enums.OrderStatus;
import com.wms.wms.exception.InsufficientStockException;
import com.wms.wms.repository.InventoryItemRepository;
import com.wms.wms.repository.OrderRepository;
import com.wms.wms.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    
    private final ProductRepository productRepository;
    
    private final InventoryItemRepository inventoryRepository;
    
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

        // ❌ Prevent duplicate deduction
        if (order.getStatus() == newStatus) {
            throw new RuntimeException("Order already in status: " + newStatus);
        }

        // ✅ Strict transition validation
        validateTransition(order.getStatus(), newStatus);

        // 🔥 CRITICAL: Deduct stock only once when moving to PACKED
        if (newStatus == OrderStatus.PACKED) {

            for (OrderItem item : order.getItems()) {

                // 🔒 LOCK the row to prevent race conditions
                InventoryItem inventory = inventoryRepository
                        .findByProduct(item.getProduct())
                        .orElseThrow(() -> new RuntimeException("Inventory not found"));

                if (inventory.getQuantity() < item.getQuantity()) {
                    throw new InsufficientStockException(
                        "Not enough stock for product: " + item.getProduct().getName()
                    );
                }

                inventory.setQuantity(
                        inventory.getQuantity() - item.getQuantity()
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
}