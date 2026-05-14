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
import com.wms.wms.dto.StorageBinRequestDTO;
import com.wms.wms.dto.StorageBinResponseDTO;
import com.wms.wms.service.StorageBinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storage-bins")
@RequiredArgsConstructor
public class StorageBinController {

    private final StorageBinService service;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StorageBinResponseDTO>> create(
            @RequestBody StorageBinRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        "Storage bin created successfully",
                        service.create(dto),
                        LocalDateTime.now()
                ));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<StorageBinResponseDTO>>> getAll() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Storage bins fetched successfully",
                        service.getAll(),
                        LocalDateTime.now()
                )
        );
    }
}