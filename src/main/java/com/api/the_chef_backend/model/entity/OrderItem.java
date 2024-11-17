package com.api.the_chef_backend.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "itens_do_pedido")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_quantity")
    private int productQuantity;
    @Column(name = "complements_quantity")
    private int complementsQuantity;

    @ManyToOne
    private Order order;
    @ManyToOne
    private Product product;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "complementos_dos_itens",
            joinColumns = @JoinColumn(name = "order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "complement_id")
    )
    private Set<ProductExtra> complements = new HashSet<>();
}