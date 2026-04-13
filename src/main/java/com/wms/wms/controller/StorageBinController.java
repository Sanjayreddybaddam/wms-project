package com.wms.wms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wms.wms.entity.StorageBin;
import com.wms.wms.service.StorageBinService;

@RestController
@RequestMapping("/api/storage-bins")
public class StorageBinController {

    private static final Logger logger = LoggerFactory.getLogger(StorageBinController.class);

    @Autowired
    private StorageBinService service;

    @PostMapping
    public StorageBin create(@RequestBody StorageBin b) {
        logger.info("Creating storage bin: {}", b.getBinCode());

        if (b.getWarehouse() != null) {
            logger.info("Assigned to warehouse ID: {}", b.getWarehouse().getId());
        } else {
            logger.warn("No warehouse assigned!");
        }

        StorageBin saved = service.create(b);

        logger.info("Storage bin created with ID: {}", saved.getId());
        return saved;
    }

    @GetMapping
    public List<StorageBin> getAll() {
        logger.info("Fetching all storage bins");

        List<StorageBin> list = service.getAll();

        logger.info("Total bins fetched: {}", list.size());
        return list;
    }
}