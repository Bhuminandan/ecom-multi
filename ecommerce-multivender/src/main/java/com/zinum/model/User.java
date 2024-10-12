package com.zinum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zinum.enums.UserRoles;
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
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "mobile", nullable = false)
    String mobile;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    UserRoles role = UserRoles.ROLE_CUSTOMER;

    @OneToMany(mappedBy = "user")
    Set<Address> addresses = new HashSet<>();

    @ManyToMany(mappedBy = "usedByUsers")
    @JsonIgnore
    Set<Coupon> usedCoupons = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    Set<Review> reviews = new HashSet<>();
}
