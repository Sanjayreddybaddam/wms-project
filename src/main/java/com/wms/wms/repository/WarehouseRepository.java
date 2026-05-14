package com.wms.wms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wms.wms.entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

	@Query("""
		    SELECT w.id, w.name, COUNT(b.id)
		    FROM Warehouse w
		    LEFT JOIN StorageBin b ON b.warehouse.id = w.id
		    GROUP BY w.id, w.name
		""")
		List<Object[]> getWarehouseWithBinCount();
}
