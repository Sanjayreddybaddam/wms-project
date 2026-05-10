package com.wms.wms.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.wms.wms.entity.StorageBin;
import com.wms.wms.exception.NoBinAvailableException;
import com.wms.wms.repository.StorageBinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PutawayService {

    private final StorageBinRepository binRepository;

    

    /**
     * Select the best bin for the given quantity in the given warehouse.
     *
     * @param warehouseId target warehouse
     * @param quantity    number of units to place
     * @return the chosen StorageBin (lock held until transaction commit)
     * @throws NoBinAvailableException if the warehouse is full
     */
    public StorageBin selectBin(Long warehouseId, int quantity) {
        List<StorageBin> availableBins = binRepository.findAvailableBins(warehouseId);

        if (availableBins.isEmpty()) {
            throw new NoBinAvailableException(
                "Warehouse " + warehouseId + " has no bins with available capacity.");
        }

        // First-fit: find a bin that fits the entire quantity
        return availableBins.stream()
            .filter(bin -> remainingCapacity(bin) >= quantity)
            .findFirst()
            // Fallback: largest available bin (partial fill — caller should handle splitting)
            .orElseGet(() -> {
                StorageBin largest = availableBins.get(0); // already sorted desc
                System.out.printf(
                    "[PUTAWAY WARN] No single bin fits %d units. Assigning largest bin %s (%d remaining).%n",
                    quantity, largest.getBinCode(), remainingCapacity(largest));
                return largest;
            });
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private int remainingCapacity(StorageBin bin) {
        return bin.getCapacity() - bin.getCurrentQuantity();
    }
}
