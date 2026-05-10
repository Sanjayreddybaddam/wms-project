package com.wms.wms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wms.wms.entity.Product;
import com.wms.wms.repository.ProductRepository;
import com.wms.wms.util.QRCodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    
    private final ProductRepository repo;

    // ✅ CREATE PRODUCT + GENERATE QR
    public Product create(Product p) {

        // Step 1: Save product first (to get ID if needed)
        Product savedProduct = repo.save(p);

        // Step 2: Generate QR using SKU
        String filePath = "src/main/resources/static/barcodes/" + savedProduct.getSku() + ".png";
        
        String savedPath = QRCodeGenerator.generateQRCode(savedProduct.getSku(), filePath);
        
        

        // Step 3: Save QR path
        savedProduct.setBarcodePath(savedPath);

        return repo.save(savedProduct);
    }

    public List<Product> getAll() {
        return repo.findAll();
    }
}