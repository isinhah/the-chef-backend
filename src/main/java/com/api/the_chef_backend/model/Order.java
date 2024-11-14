package com.api.the_chef_backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "pedidos")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String waiter;

    @CreationTimestamp
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalDateTime time;

    @Column(scale = 2, nullable = false)
    private BigDecimal subtotal;
    @Column(scale = 2, name = "price_with_commission", nullable = false)
    private BigDecimal priceWithCommission;
    @Column(scale = 2, nullable = false)
    private BigDecimal total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderItem> items = new HashSet<>();

    @ManyToOne
    private RestaurantTable table;
}
