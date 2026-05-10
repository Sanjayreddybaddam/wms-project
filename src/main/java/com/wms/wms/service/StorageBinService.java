package com.wms.wms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wms.wms.entity.StorageBin;
import com.wms.wms.repository.StorageBinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageBinService {

    private final StorageBinRepository repo;

    public StorageBin create(StorageBin b) {
        return repo.save(b);
    }

    public List<StorageBin> getAll() {
        return repo.findAll();
    }
}