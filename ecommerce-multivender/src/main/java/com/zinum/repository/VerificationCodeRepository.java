package com.zinum.repository;

import com.zinum.enums.UserRoles;
import com.zinum.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByEmailAndUserType(String email, UserRoles userType);
    VerificationCode findByCodeAndUserType(String otp, UserRoles userType);

    VerificationCode findByEmail(String email);
}
