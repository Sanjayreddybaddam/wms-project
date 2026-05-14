package com.wms.wms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String sku;

    private String barcodePath;
    
    @Min(value = 1, message = "Price must be greater than 0")
    private double price;

	private boolean active = true;

//    @Min(value = 0, message = "Stock cannot be negative")
//    private Integer stock;
}
