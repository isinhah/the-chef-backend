package com.api.the_chef_backend.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "pedidos")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String waiter;

    @CreationTimestamp
    private LocalDateTime startTime;

    @Column(scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> items = new HashSet<>();

    @ManyToOne
    private RestaurantTable table;
    @ManyToOne
    private Restaurant restaurant;

    public void calculateTotalPrice() {
        total = BigDecimal.ZERO;

        if (items != null) {
            for (OrderItem item : items) {

                BigDecimal productTotal = item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getProductQuantity()));

                BigDecimal complementsTotal = BigDecimal.ZERO;
                if (item.getComplements() != null) {
                    complementsTotal = item.getComplements().stream()
                            .map(ProductExtra::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .multiply(BigDecimal.valueOf(item.getComplementsQuantity()));
                }

                total = total.add(productTotal).add(complementsTotal);
            }
        }
    }
}