package com.wms.wms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wms.wms.entity.Warehouse;
import com.wms.wms.service.WarehouseService;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);

    @Autowired
    private WarehouseService service;

    @PostMapping
    public Warehouse create(@RequestBody Warehouse w) {
        logger.info("Creating warehouse with name: {}", w.getName());

        Warehouse saved = service.create(w);

        logger.info("Warehouse created successfully with ID: {}", saved.getId());
        return saved;
    }

    @GetMapping
    public List<Warehouse> getAll() {
        logger.info("Fetching all warehouses");

        List<Warehouse> list = service.getAll();

        logger.info("Total warehouses fetched: {}", list.size());
        return list;
    }
}