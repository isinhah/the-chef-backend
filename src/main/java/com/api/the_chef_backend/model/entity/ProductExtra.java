package com.api.the_chef_backend.model.entity;

import com.api.the_chef_backend.model.dtos.request.ProductExtraRequestDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "complementos")
public class ProductExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(scale = 2)
    private BigDecimal price;
    @Column(name = "quantity_in_stock")
    private int stock;

    @ManyToOne
    private Restaurant restaurant;

    @JsonBackReference
    @ManyToMany(mappedBy = "complements", fetch = FetchType.EAGER)
    private Set<OrderItem> items = new HashSet<>();

    public void alterComplement(ProductExtraRequestDTO dto, Restaurant restaurant) {
        this.name = dto.name();
        this.price = dto.price();
        this.stock = dto.stock();

        if (restaurant != null) {
            this.restaurant = restaurant;
        }
    }
}