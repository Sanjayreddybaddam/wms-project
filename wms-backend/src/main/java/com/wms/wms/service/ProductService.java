package com.wms.wms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wms.wms.dto.ProductResponseDTO;
import com.wms.wms.entity.InventoryItem;
import com.wms.wms.entity.Product;
import com.wms.wms.entity.StorageBin;
import com.wms.wms.repository.InventoryItemRepository;
import com.wms.wms.repository.OrderItemRepository;
import com.wms.wms.repository.ProductRepository;
import com.wms.wms.repository.StorageBinRepository;
import com.wms.wms.repository.UserRepository;
import com.wms.wms.util.QRCodeGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final InventoryItemRepository inventoryItemRepository;
    private final ProductRepository productRepository;
    private final StorageBinRepository storageBinRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    // =========================
    // CREATE PRODUCT
    // =========================
    @Transactional
    public ProductResponseDTO create(Product p, Integer initialStock, Long storageBinId) {

        Product saved = productRepository.save(p);

        try {
            String filePath = "src/main/resources/static/barcodes/" + saved.getSku() + ".png";
            String qrPath = QRCodeGenerator.generateQRCode(saved.getSku(), filePath);

            saved.setBarcodePath(qrPath);
            saved = productRepository.save(saved);

        } catch (Exception e) {
            throw new RuntimeException("QR generation failed", e);
        }

        // ✅ SAFE FETCH OF BIN (FIXED)
        StorageBin bin = storageBinRepository.findById(storageBinId)
                .orElseThrow(() -> new RuntimeException("Storage bin not found"));

        // check if inventory already exists
        InventoryItem existing = inventoryItemRepository
                .findByProductIdAndStorageBinId(saved.getId(), storageBinId)
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + initialStock);
            inventoryItemRepository.save(existing);
        } else {
            InventoryItem item = new InventoryItem();
            item.setProduct(saved);
            item.setStorageBin(bin);
            item.setQuantity(initialStock);

            inventoryItemRepository.save(item);
        }

        return mapToDTO(saved, initialStock);
    }

    // =========================
    // GET PRODUCTS WITH STOCK
    // =========================
    public List<ProductResponseDTO> getProducts(Long warehouseId) {

    	List<Product> products = productRepository.findByActiveTrue();

        List<Object[]> stockData =
                inventoryItemRepository.getStockByWarehouse(warehouseId);

        Map<Long, Integer> stockMap = new HashMap<>();

        for (Object[] row : stockData) {
            Long productId = (Long) row[0];
            Integer stock = ((Number) row[1]).intValue();
            stockMap.put(productId, stock);
        }

        return products.stream()
                .map(p -> mapToDTO(p, stockMap.getOrDefault(p.getId(), 0)))
                .toList();
    }

    // =========================
    // UPDATE PRODUCT
    // =========================
    public ProductResponseDTO update(Long id, Product updated) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(updated.getName());
        existing.setSku(updated.getSku());
        existing.setPrice(updated.getPrice());

        Product saved = productRepository.save(existing);

        return mapToDTO(saved, 0);
    }

    // =========================
    // DELETE PRODUCT (SOFT DELETE)
    // =========================
    @Transactional
    public void delete(Long id) {

        orderItemRepository.deleteByProductId(id);
        inventoryItemRepository.deleteByProductId(id);

        productRepository.deleteById(id);
    }

    // =========================
    // DTO MAPPER
    // =========================
    public ProductResponseDTO mapToDTO(Product product, Integer stock) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getSku(),
                stock,
                product.getBarcodePath(),
                product.getPrice()
        );
    }

    public Long getWarehouseIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getWarehouse()
                .getId();
    }
}