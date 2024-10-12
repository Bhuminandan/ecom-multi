package com.zinum.model;

import com.zinum.enums.PaymentMethod;
import com.zinum.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payment_order")
public class PaymentOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "amount", nullable = false)
    Long amount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    @Column(name = "payment_method", nullable = false)
    PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "payment")
    Set<Order> orders = new HashSet<>();
}
