package com.wms.wms.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wms.wms.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;
    
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
}
