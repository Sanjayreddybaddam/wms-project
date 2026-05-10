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
import com.wms.wms.entity.Product;
import com.wms.wms.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> create(@RequestBody Product p) {

        Product saved = service.create(p);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        "Product created successfully",
                        saved,
                        LocalDateTime.now()
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAll() {

        List<Product> list = service.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Products fetched successfully",
                        list,
                        LocalDateTime.now()
                )
        );
    }
}