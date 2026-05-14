package com.wms.wms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wms.wms.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // ✅ used during product delete to avoid FK constraint error
    void deleteByProductId(Long productId);
}