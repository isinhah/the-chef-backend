package com.api.the_chef_backend.model.entity;

import com.api.the_chef_backend.model.dtos.request.RestaurantRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "restaurantes")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, name = "cpf_or_cnpj", nullable = false)
    private String cpfCnpj;
    @Column(unique = true, nullable = false)
    private String phone;

    @Column(name = "table_quantity")
    private int tableQuantity;
    @Column(name = "waiter_commission")
    private BigDecimal waiterCommission;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @OrderBy("tableNumber")
    private Set<RestaurantTable> tables = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    public void alterRestaurant(RestaurantRequestDTO dto) {
        if (dto.name() != null) {
            this.name = dto.name();
        }
        if (dto.cpfOrCnpj() != null) {
            this.cpfCnpj = dto.cpfOrCnpj();
        }
        if (dto.phone() != null) {
            this.phone = dto.phone();
        }
        if (dto.waiterCommission() != null) {
            this.waiterCommission = dto.waiterCommission();
        }
        this.tableQuantity = dto.tableQuantity();
    }
}