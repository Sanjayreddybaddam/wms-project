package com.wms.wms.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Warehouse {
	
    @Id 
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<StorageBin> bins;
}
