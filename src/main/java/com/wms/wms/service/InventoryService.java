package com.wms.wms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wms.wms.entity.InventoryItem;
import com.wms.wms.repository.InventoryItemRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryItemRepository inventoryRepository;

    // TOTAL STOCK (warehouse-aware)
    public int getTotalStock(Long productId, Long warehouseId) {
        return inventoryRepository.totalStockForProduct(productId, warehouseId);
    }

    // DEDUCT STOCK (WAREHOUSE + BIN SAFE)
    @Transactional
    public void deductStock(Long productId, Long warehouseId, int qty) {

        List<InventoryItem> items =
                inventoryRepository.findByProductIdAndStorageBin_WarehouseIdOrderByIdAsc(
                        productId,
                        warehouseId
                );

        int remaining = qty;

        for (InventoryItem item : items) {

            if (remaining <= 0) break;

            int available = item.getQuantity();

            if (available <= 0) continue;

            int deduct = Math.min(available, remaining);

            item.setQuantity(available - deduct);
            inventoryRepository.save(item);

            remaining -= deduct;
        }

        if (remaining > 0) {
            throw new RuntimeException("Insufficient stock across bins");
        }
    }
}