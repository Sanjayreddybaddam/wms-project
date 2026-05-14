package com.wms.wms.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wms.wms.dto.StorageBinRequestDTO;
import com.wms.wms.dto.StorageBinResponseDTO;
import com.wms.wms.entity.StorageBin;
import com.wms.wms.entity.User;
import com.wms.wms.repository.StorageBinRepository;
import com.wms.wms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageBinService {

    private final StorageBinRepository repo;
    private final UserRepository userRepository;

    public StorageBinResponseDTO create(StorageBinRequestDTO dto) {

        User user = getLoggedInUser();

        StorageBin bin = new StorageBin();
        bin.setBinCode(dto.getBinCode());
        bin.setCapacity(dto.getCapacity());
        bin.setCurrentQuantity(0);
        bin.setWarehouse(user.getWarehouse());

        StorageBin saved = repo.save(bin);

        return toDTO(saved);
    }

    public List<StorageBinResponseDTO> getAll() {

        User user = getLoggedInUser();

        return repo.findByWarehouseId(user.getWarehouse().getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }
    
    private User getLoggedInUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    private StorageBinResponseDTO toDTO(StorageBin bin) {
        return new StorageBinResponseDTO(
                bin.getId(),
                bin.getBinCode(),
                bin.getCapacity(),
                bin.getCurrentQuantity(),
                bin.getWarehouse().getId()
        );
    }
}