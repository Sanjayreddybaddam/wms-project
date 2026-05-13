package com.wms.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wms.wms.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserUsername(String username);
}
