package com.zinum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "coupons")
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    String code;

    @Column(name = "discount", nullable = false)
    double discountPercentage;

    @Column(name = "validity_start_date", nullable = false)
    LocalDate validityStartDate;

    @Column(name = "validity_end_date", nullable = false)
    LocalDate validityEndDate;

    @Column(name = "minimum_order_amount", nullable = false)
    double minimumOrderAmount;

    @Column(name = "is_active", nullable = false)
    boolean isActive = true;

    @ManyToMany
    @JoinTable(
            name = "used_coupons",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> usedByUsers = new HashSet<>();
}
