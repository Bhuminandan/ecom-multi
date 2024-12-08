package com.zinum.model;

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
@Table(name = "seller_reports")
public class SellerReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "total_earnings", nullable = false)
    Long totalEarnings = 0L;

    @Column(name = "total_sales", nullable = false)
    Long totalSales = 0L;

    @Column(name = "total_refunds", nullable = false)
    Long totalRefunds = 0L;

    @Column(name = "total_tax", nullable = false)
    Long totalTax = 0L;

    @Column(name = "net_earnings", nullable = false)
    Long netEarnings = 0L;

    @Column(name = "total_orders", nullable = false)
    Integer totalOrders = 0;

    @Column(name = "canceled_orders", nullable = false)
    Integer canceledOrders = 0;

    @Column(name = "total_transactions", nullable = false)
    Integer totalTransactions = 0;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "seller_id")
    Seller seller;

}
