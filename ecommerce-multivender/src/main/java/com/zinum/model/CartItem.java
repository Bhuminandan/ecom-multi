package com.zinum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cart_items")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    Cart cart;


    @Column(name = "size", nullable = false)
    String size;

    @Column(name = "quantity", nullable = false)
    int quantity=1;

    @Column(name = "mrpPrice", nullable = false)
    Integer mrpPrice;

    @Column(name = "discountedPrice", nullable = false)
    Integer discountedPrice;

    long userId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;
}
