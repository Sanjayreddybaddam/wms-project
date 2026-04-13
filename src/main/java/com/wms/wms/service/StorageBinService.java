package com.wms.wms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wms.wms.entity.StorageBin;
import com.wms.wms.repository.StorageBinRepository;

@Service
public class StorageBinService {

    @Autowired
    private StorageBinRepository repo;

    public StorageBin create(StorageBin b) {
        return repo.save(b);
    }

    public List<StorageBin> getAll() {
        return repo.findAll();
    }
}