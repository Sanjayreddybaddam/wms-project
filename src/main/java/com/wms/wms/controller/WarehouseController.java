package com.wms.wms.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wms.wms.dto.ApiResponse;
import com.wms.wms.entity.Warehouse;
import com.wms.wms.service.WarehouseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Warehouse>> create(@RequestBody Warehouse w) {

        Warehouse saved = service.create(w);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        "Warehouse created successfully",
                        saved,
                        LocalDateTime.now()
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Warehouse>>> getAll() {

        List<Warehouse> list = service.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Warehouses fetched successfully",
                        list,
                        LocalDateTime.now()
                )
        );
    }
}