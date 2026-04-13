package com.wms.wms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wms.wms.entity.InventoryItem;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

}
