package com.api.the_chef_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "complementos")
public class ProductExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(scale = 2)
    private BigDecimal price;
    @Column(name = "quantity_in_stock")
    private int stock;

    @ManyToMany(mappedBy = "complements")
    private Set<Product> products = new HashSet<>();

    @ManyToMany(mappedBy = "complements")
    private Set<OrderItem> items = new HashSet<>();
}
