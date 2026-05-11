package com.wms.wms.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wms.wms.dto.ApiResponse;
import com.wms.wms.dto.ProductRequestDTO;
import com.wms.wms.dto.ProductResponseDTO;
import com.wms.wms.entity.Product;
import com.wms.wms.repository.ProductRepository;
import com.wms.wms.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    
    private final ProductService productService;

    // ✅ ADMIN ONLY → Add product
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addProduct(@RequestBody Product product) {

        Product saved = productRepository.save(product);

        ProductResponseDTO response = productService.mapToDTO(saved);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        "Product added successfully",
                        response,
                        LocalDateTime.now()
                ));
    }

    // ✅ ADMIN ONLY → Update product
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO updated) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(updated.getName());
        product.setSku(updated.getSku());

        Product saved = productRepository.save(product);

        ProductResponseDTO response = productService.mapToDTO(saved);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Product updated successfully",
                        response,
                        LocalDateTime.now()
                )
        );
    }

    // ✅ BOTH ADMIN & OPERATOR → View products
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAll() {

        List<ProductResponseDTO> products = productService.getAll();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Products fetched successfully",
                        products,
                        LocalDateTime.now()
                )
        );
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
    
    
}