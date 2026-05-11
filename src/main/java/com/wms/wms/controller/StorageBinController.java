package com.wms.wms.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wms.wms.dto.ApiResponse;
import com.wms.wms.entity.StorageBin;
import com.wms.wms.service.StorageBinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storage-bins")
@RequiredArgsConstructor
public class StorageBinController {

    private final StorageBinService service;
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<StorageBin>> create(@RequestBody StorageBin b) {

        StorageBin saved = service.create(b);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        "Storage bin created successfully",
                        saved,
                        LocalDateTime.now()
                ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StorageBin>>> getAll() {

        List<StorageBin> list = service.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Storage bins fetched successfully",
                        list,
                        LocalDateTime.now()
                )
        );
    }
}