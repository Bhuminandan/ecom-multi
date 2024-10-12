package com.zinum.model;

import com.zinum.enums.OrderStatus;
import com.zinum.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "order_id", nullable = false)
    String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "seller_id")
    Long sellerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "address_id")
    Address shippingAddress;

    @Embedded
    PaymentDetails paymentDetails = new PaymentDetails();

    @Column(name = "total_mrp_price")
    double totalMrpPrice;

    @Column(name = "total_selling_price")
    double totalSellingPrice;

    @Column(name = "total_discount")
    double totalDiscount;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "order_date")
    LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "delivery_date")
    LocalDateTime deliveryDate = orderDate.plusDays(7);
}