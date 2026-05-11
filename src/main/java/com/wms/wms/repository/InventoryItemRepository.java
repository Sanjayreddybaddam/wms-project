package com.wms.wms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wms.wms.entity.InventoryItem;
import com.wms.wms.entity.Product;

import jakarta.persistence.LockModeType;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    /**
     * Find existing inventory record for a product in a specific bin.
     * Used to decide whether to create a new record or increment an existing one.
     */
    Optional<InventoryItem> findByProductIdAndStorageBinId(Long productId, Long storageBinId);

    /**
     * Atomically increment the quantity of an existing inventory item.
     * Avoids a read-modify-write race condition by doing the math in the DB.
     */
    @Modifying
    @Query("""
        UPDATE InventoryItem i
        SET i.quantity = i.quantity + :delta
        WHERE i.id = :id
        """)
    int incrementQuantity(@Param("id") Long id, @Param("delta") int delta);

    /**
     * Total stock for a product across all bins in a warehouse.
     */
    @Query("""
        SELECT COALESCE(SUM(i.quantity), 0)
        FROM InventoryItem i
        WHERE i.product.id = :productId
          AND i.storageBin.warehouse.id = :warehouseId
        """)
    int totalStockForProduct(@Param("productId") Long productId,
                             @Param("warehouseId") Long warehouseId);



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<InventoryItem> findByProduct(Product product);
    
    Optional<InventoryItem> findByProductIdAndStorageBin_WarehouseId(
    	    Long productId,
    	    Long warehouseId
    	);


	List<InventoryItem> findByProductIdAndStorageBin_WarehouseIdOrderByIdAsc(Long productId, Long warehouseId);

}
