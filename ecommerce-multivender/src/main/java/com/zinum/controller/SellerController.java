package com.zinum.controller;

import com.zinum.Utils.OtpUtil;
import com.zinum.config.JwtProvider;
import com.zinum.dto.request.LoginReq;
import com.zinum.dto.response.AuthRes;
import com.zinum.enums.AccountStatus;
import com.zinum.enums.UserRoles;
import com.zinum.exception.SellerException;
import com.zinum.model.Seller;
import com.zinum.model.VerificationCode;
import com.zinum.repository.VerificationCodeRepository;
import com.zinum.service.Impl.AuthServiceImpl;
import com.zinum.service.Impl.EmailServiceImpl;
import com.zinum.service.Impl.SellerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
@Slf4j
public class SellerController {
    private final SellerServiceImpl sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthServiceImpl authService;
    private final EmailServiceImpl emailService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthRes> loginSeller(@RequestBody LoginReq loginReq) throws Exception {
        String email = loginReq.getEmail();
        loginReq.setEmail("seller_" + email);
        log.info("Logging in seller>>>>>>>>>>>>: {}", loginReq);
        AuthRes res = authService.signin(loginReq, UserRoles.ROLE_SELLER);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) {
        VerificationCode verificationCode = verificationCodeRepository.findByCodeAndUserType(otp, UserRoles.ROLE_SELLER);
        if (verificationCode == null || !verificationCode.getCode().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        Seller seller = sellerService.verifySellerEmail(verificationCode.getEmail(), otp);
        return ResponseEntity.ok(seller);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCode(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCode.setUserType(UserRoles.ROLE_SELLER);
        verificationCodeRepository.save(verificationCode);

        String subject = "Email verification code";
        String body = "Please click on the link to verify your email: ";
        String frontendUrl = "http://localhost:4200/verify/";
        emailService.sendOtpVerificationEmail(seller.getEmail(), otp, subject, body + frontendUrl);

        return ResponseEntity.ok(savedSeller);
    }

    @GetMapping("{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwtToken(
        @RequestHeader("Authorization") String token
    ) {
        Seller seller = sellerService.getSellerProfile(token);
        return ResponseEntity.ok(seller);
    }

//    @GetMapping("/report")
//    public ResponseEntity<SellerReport> getSellerProfile(
//        @RequestHeader("Authorization") String token
//    ) {
//        String email = jwtProvider.getEmailFromJWT(token);
//        SellerReport report = sellerService.getSellerReport(email);
//        return ResponseEntity.ok(report);
//    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus status) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String token,
           @RequestBody Seller seller) throws SellerException {
        Seller profile = sellerService.getSellerProfile(token);
        seller.setId(profile.getId());
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws SellerException {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
