package com.wms.wms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class StorageBin {

    @Id 
    @GeneratedValue
    private Long id;

    private String binCode;
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    @JsonBackReference
    private Warehouse warehouse;
}

