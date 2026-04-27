package com.wms.wms.service;

import com.wms.wms.dto.ReceivingRequest;
import com.wms.wms.dto.ReceivingResponse;
import com.wms.wms.entity.InventoryItem;
import com.wms.wms.entity.Product;
import com.wms.wms.entity.StorageBin;
import com.wms.wms.entity.Warehouse;
import com.wms.wms.exception.ResourceNotFoundException;
import com.wms.wms.repository.InventoryItemRepository;
import com.wms.wms.repository.ProductRepository;
import com.wms.wms.repository.StorageBinRepository;
import com.wms.wms.repository.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReceivingService — core Week 2 business logic.
 *
 * The @Transactional annotation makes ALL four steps below atomic:
 *
 *   Step 1 → Validate product and warehouse exist
 *   Step 2 → Select optimal bin via PutawayService (DB lock acquired)
 *   Step 3 → Update bin's currentQuantity
 *   Step 4 → Create or increment InventoryItem record
 *
 * If ANY step throws an exception, all DB changes since Step 1 are
 * rolled back automatically — inventory is never left in a partial state.
 *
 * Isolation.REPEATABLE_READ prevents another transaction from modifying
 * a bin's currentQuantity between the time we read it and when we commit.
 */
@Service
public class ReceivingService {

    private static final Logger logger = LoggerFactory.getLogger(ReceivingService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private StorageBinRepository storageBinRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private PutawayService putawayService;

    /**
     * Process an inbound shipment as a single atomic database operation.
     *
     * @param request contains productId, warehouseId, quantity, optional supplierReference
     * @return ReceivingResponse with assigned bin details
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public ReceivingResponse receiveShipment(ReceivingRequest request) {

        logger.info("Starting receiving process: productId={}, warehouseId={}, qty={}",
            request.getProductId(), request.getWarehouseId(), request.getQuantity(), request.getSupplierReference());

        // ── Step 1: Validate product and warehouse ────────────────────────────
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Product not found with id: " + request.getProductId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Warehouse not found with id: " + request.getWarehouseId()));

        // ── Step 2: Select optimal bin (putaway algorithm, lock acquired) ─────
        StorageBin bin = putawayService.selectBin(warehouse.getId(), request.getQuantity());
        logger.info("Putaway: assigned to bin '{}' in warehouse '{}'",
            bin.getBinCode(), warehouse.getName());

        // ── Step 3: Update bin occupancy ──────────────────────────────────────
        // Cap quantity to whatever space actually remains in the chosen bin
        int spaceRemaining = bin.getCapacity() - bin.getCurrentQuantity();
        int effectiveQty = Math.min(request.getQuantity(), spaceRemaining);

        bin.setCurrentQuantity(bin.getCurrentQuantity() + effectiveQty);
        storageBinRepository.save(bin);

        // ── Step 4: Create or increment InventoryItem ─────────────────────────
        InventoryItem item = inventoryItemRepository
            .findByProductIdAndStorageBinId(product.getId(), bin.getId())
            .orElseGet(() -> {
                // First time this product is placed in this bin → create new record
                InventoryItem newItem = new InventoryItem();
                newItem.setProduct(product);
                newItem.setStorageBin(bin);
                newItem.setQuantity(0);
                return newItem;
            });

        item.setQuantity(item.getQuantity() + effectiveQty);
        InventoryItem saved = inventoryItemRepository.save(item);

        logger.info("Receiving complete: inventoryItemId={}, qty={}, bin={}",
            saved.getId(), effectiveQty, bin.getBinCode());

        // ── Build response ────────────────────────────────────────────────────
        ReceivingResponse response = new ReceivingResponse();
        response.setInventoryItemId(saved.getId());
        response.setProductName(product.getName());
        response.setProductSku(product.getSku());
        response.setQuantityReceived(effectiveQty);
        response.setAssignedBinCode(bin.getBinCode());
        response.setWarehouseName(warehouse.getName());
        response.setSupplierReference(request.getSupplierReference());
        response.setMessage(String.format(
            "Successfully received %d unit(s) of '%s' into bin '%s'.",
            effectiveQty, product.getName(), bin.getBinCode()
        ));

        return response;
    }
}