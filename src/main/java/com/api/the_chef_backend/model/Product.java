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
@Table(name = "produtos")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String imageUrl;
    private String description;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "pdv_code")
    private String pdvCode;

    @Column(name = "quantity_in_stock")
    private int stock;

    @Column(name = "weight_in_kg")
    private BigDecimal weightKg;

    @ManyToOne
    private Category category;

    @ManyToMany
    private Set<ProductExtra> complements = new HashSet<>();
}