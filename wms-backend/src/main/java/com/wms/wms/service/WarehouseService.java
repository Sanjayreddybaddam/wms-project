package com.wms.wms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wms.wms.dto.WarehouseResponseDTO;
import com.wms.wms.entity.Warehouse;
import com.wms.wms.repository.WarehouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseService {


    private final WarehouseRepository repo;

    public Warehouse create(Warehouse w) {
        return repo.save(w);
    }

    public List<WarehouseResponseDTO> getAll() {

        List<Object[]> result = repo.getWarehouseWithBinCount();

        return result.stream()
                .map(row -> new WarehouseResponseDTO(
                        (Long) row[0],
                        (String) row[1],
                        ((Long) row[2]).intValue()
                ))
                .toList();
    }
}
