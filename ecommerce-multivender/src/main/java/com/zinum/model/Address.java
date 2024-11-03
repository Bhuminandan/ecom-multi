package com.zinum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.zinum.enums.AddressTypes;
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
@Table(name = "addresses")
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "locality", nullable = false)
    String locality;

    @Column(name = "city", nullable = false)
    String city;

    @Column(name = "state", nullable = false)
    String state;

    @Column(name = "country", nullable = false)
    String country;

    @Column(name = "pin_code", nullable = false)
    String pinCode;

    @Column(name = "address", nullable = false)
    String address;

    @Column(name = "mobile", nullable = false)
    String mobile;

    @Column(name = "type", nullable = false)
    AddressTypes type = AddressTypes.HOME;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne
    @JoinColumn(name = "seller_id")
    @JsonBackReference
    Seller seller;

    @OneToMany(mappedBy = "shippingAddress")
    Set<Order> orders = new HashSet<>();
}
