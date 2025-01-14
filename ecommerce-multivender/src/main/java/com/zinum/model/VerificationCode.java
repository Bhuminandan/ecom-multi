package com.zinum.model;

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
@Table(name = "verification_code")
public class VerificationCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(name = "code", nullable = false)
    String code;

    @Column(name = "email", nullable = false)
    String email;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "user_id")
    User user;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "seller_id")
    Seller seller;

    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    UserRoles userType = UserRoles.ROLE_CUSTOMER;
}
