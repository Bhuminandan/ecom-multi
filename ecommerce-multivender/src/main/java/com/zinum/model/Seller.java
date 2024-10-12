package com.zinum.model;

import com.zinum.enums.AccountStatus;
import com.zinum.enums.UserRoles;
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
@Table(name = "sellers")
public class Seller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "email", nullable = false,unique = true)
    String email;

    @Column(name = "mobile", nullable = false)
    String mobile;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "GSTIN", nullable = false)
    String GSTIN;

    @Embedded
    BusinessDataDetails businessDataDetails = new BusinessDataDetails();

    @Embedded
    BankDetails bankDetails = new BankDetails();

    @OneToOne(cascade = CascadeType.ALL)
    Address pickupAddress = new Address();

    @Column(name = "role", nullable = false)
    UserRoles role = UserRoles.ROLE_SELLER;

    @Column(name = "is_verified", nullable = false)
    boolean isVerified = false;

    @Column(name = "account_status", nullable = false)
    AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;

}
