package com.api.the_chef_backend.model.entity;

import com.api.the_chef_backend.model.dtos.request.ProductRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "produtos")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne
    private Category category;
    @ManyToOne
    private Restaurant restaurant;

    public void alterProduct(ProductRequestDTO dto, Restaurant restaurant, Category category) {
        this.name = dto.name();
        this.price = dto.price();

        if (dto.imageUrl() != null) {
            this.imageUrl = dto.imageUrl();
        }
        if (dto.description() != null) {
            this.description = dto.description();
        }
        if (dto.pdvCode() != null) {
            this.pdvCode = dto.pdvCode();
        }
        if (category != null) {
            this.category = category;
        }
        if (restaurant != null) {
            this.restaurant = restaurant;
        }
    }
}