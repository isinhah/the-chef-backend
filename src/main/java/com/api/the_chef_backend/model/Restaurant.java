package com.api.the_chef_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

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
    @Column(unique = true, nullable = false)
    private String cpfOrCnpj;
    @Column(nullable = false)
    private String phone;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(name = "table_quantity")
    private int tableQuantity;
    @Column(name = "waiter_commission")
    private BigDecimal waiterCommission;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @OrderBy("number")
    private Set<RestaurantTable> tables = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();
}
