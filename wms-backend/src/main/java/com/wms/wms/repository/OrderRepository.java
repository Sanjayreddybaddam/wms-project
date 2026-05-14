package com.wms.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wms.wms.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("""
			SELECT DISTINCT o FROM Order o
			LEFT JOIN FETCH o.items i
			LEFT JOIN FETCH i.product
			LEFT JOIN FETCH o.user
			WHERE o.user.username = :username
			""")
			List<Order> findOrdersWithItems(@Param("username") String username);
}
