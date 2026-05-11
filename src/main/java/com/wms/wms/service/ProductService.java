package com.wms.wms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wms.wms.dto.ProductResponseDTO;
import com.wms.wms.entity.Product;
import com.wms.wms.repository.ProductRepository;
import com.wms.wms.util.QRCodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    // ✅ CREATE PRODUCT + QR GENERATION
    public ProductResponseDTO create(Product p) {

        Product saved = repo.save(p);

        try {
            String filePath = System.getProperty("user.dir")
                    + "/barcodes/" + saved.getSku() + ".png";

            String qrPath = QRCodeGenerator.generateQRCode(saved.getSku(), filePath);

            saved.setBarcodePath(qrPath);

            saved = repo.save(saved);

        } catch (Exception e) {
            throw new RuntimeException("QR generation failed", e);
        }

        return mapToDTO(saved);
    }

    // ✅ GET ALL PRODUCTS
    public List<ProductResponseDTO> getAll() {

        return repo.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ✅ UPDATE PRODUCT
    public ProductResponseDTO update(Long id, Product updated) {

        Product existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(updated.getName());
        existing.setSku(updated.getSku());

        Product saved = repo.save(existing);

        return mapToDTO(saved);
    }

    // ✅ DELETE PRODUCT
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ✅ DTO MAPPER
    public ProductResponseDTO mapToDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getSku()
        );
    }
}