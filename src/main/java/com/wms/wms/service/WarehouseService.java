package com.wms.wms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wms.wms.entity.Warehouse;
import com.wms.wms.repository.WarehouseRepository;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository repo;

    public Warehouse create(Warehouse w) {
        return repo.save(w);
    }

    public List<Warehouse> getAll() {
        return repo.findAll();
    }
}
