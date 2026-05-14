package com.wms.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wms.wms.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByActiveTrue();
}
