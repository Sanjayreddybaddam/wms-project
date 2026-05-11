package com.wms.wms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private StorageBin storageBin;

    private int quantity;
}
