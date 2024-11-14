package com.api.the_chef_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "itens_do_pedido")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "products_quantity")
    private int productsQuantity;
    @Column(name = "complements_quantity")
    private int complementsQuantity;

    @ManyToOne
    private Order order;
    @ManyToOne
    private Product product;

    @ManyToMany
    @JoinTable(
            name = "complementos_do_pedido",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "product_complement_id")
    )
    private Set<ProductExtra> complements = new HashSet<>();
}
