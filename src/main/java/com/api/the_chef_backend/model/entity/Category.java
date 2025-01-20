package com.api.the_chef_backend.model.entity;

import com.api.the_chef_backend.model.dtos.request.CategoryRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "categorias")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @ToString.Exclude
    private Restaurant restaurant;

    public void alterCategory(CategoryRequestDTO dto, Restaurant restaurant) {
        if (dto.name() != null) {
            this.name = dto.name();
        }
        if (restaurant != null) {
            this.restaurant = restaurant;
        }
    }
}