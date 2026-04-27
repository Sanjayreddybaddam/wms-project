package com.wms.wms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wms.wms.entity.StorageBin;

import jakarta.persistence.LockModeType;

@Repository
public interface StorageBinRepository extends JpaRepository<StorageBin, Long> {

    /**
     * Find all bins in a warehouse that still have available capacity,
     * ordered by available space descending (best-fit first).
     * PESSIMISTIC_WRITE lock prevents two concurrent threads from
     * picking the same bin simultaneously.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT b FROM StorageBin b
        WHERE b.warehouse.id = :warehouseId
          AND b.currentQuantity < b.capacity
        ORDER BY (b.capacity - b.currentQuantity) DESC
        """)
    List<StorageBin> findAvailableBins(@Param("warehouseId") Long warehouseId);

    /**
     * Find a specific bin by its bin code within a warehouse.
     */
    Optional<StorageBin> findByBinCodeAndWarehouseId(String binCode, Long warehouseId);

    /**
     * Count occupied bins in a warehouse (useful for dashboard metrics).
     */
    @Query("""
        SELECT COUNT(b) FROM StorageBin b
        WHERE b.warehouse.id = :warehouseId
          AND b.currentQuantity > 0
        """)
    long countOccupiedBins(@Param("warehouseId") Long warehouseId);
}