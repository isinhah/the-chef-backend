package com.api.the_chef_backend.model.entity;

import com.api.the_chef_backend.model.dtos.request.RestaurantTableRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "mesas")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(name = "table_number")
    private int tableNumber;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @ManyToOne
    private Restaurant restaurant;

    public void alterTable(RestaurantTableRequestDTO dto, Restaurant restaurant) {
        if (dto.name() != null) {
            this.name = dto.name();
        }
        if (dto.restaurantId() != null) {
            this.restaurant = restaurant;
        }
        this.tableNumber = dto.tableNumber();
    }
}
