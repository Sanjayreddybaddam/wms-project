package com.wms.wms.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wms.wms.dto.ApiResponse;
import com.wms.wms.dto.ProductCreateRequest;
import com.wms.wms.dto.ProductRequestDTO;
import com.wms.wms.dto.ProductResponseDTO;
import com.wms.wms.entity.Product;
import com.wms.wms.entity.User;
import com.wms.wms.repository.UserRepository;
import com.wms.wms.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;

    // ✅ CREATE PRODUCT
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addProduct(
            @RequestBody ProductCreateRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product p = new Product();
        p.setName(request.getName());
        p.setSku(request.getSku());
        p.setPrice(request.getPrice());

        ProductResponseDTO response =
                productService.create(p, request.getStock(), request.getStorageBinId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Product created", response, LocalDateTime.now()));
    }

    // ✅ UPDATE PRODUCT
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO updated) {

        Product p = new Product();
        p.setName(updated.getName());
        p.setSku(updated.getSku());
        p.setPrice(updated.getPrice());

        ProductResponseDTO response = productService.update(id, p);

        return ResponseEntity.ok(
                new ApiResponse<>("Product updated successfully", response, LocalDateTime.now())
        );
    }

    // ✅ GET PRODUCTS (ADMIN + OPERATOR SAFE)
 // GET PRODUCTS (ADMIN + OPERATOR SAFE)
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAll(
            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long warehouseId = user.getWarehouse().getId();

        List<ProductResponseDTO> products =
                productService.getProducts(warehouseId);

        return ResponseEntity.ok(
                new ApiResponse<>("Products fetched successfully", products, LocalDateTime.now())
        );
    }

    // ✅ DELETE
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        productService.delete(id);

        return ResponseEntity.ok("Deleted successfully");
    }
}