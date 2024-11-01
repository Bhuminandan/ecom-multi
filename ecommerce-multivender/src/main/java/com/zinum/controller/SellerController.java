package com.zinum.controller;

import com.zinum.dto.request.LoginOtpReq;
import com.zinum.dto.request.LoginReq;
import com.zinum.dto.response.ApiResponse;
import com.zinum.dto.response.AuthRes;
import com.zinum.model.VerificationCode;
import com.zinum.repository.VerificationCodeRepository;
import com.zinum.service.Impl.AuthServiceImpl;
import com.zinum.service.Impl.SellerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final SellerServiceImpl sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<AuthRes> loginSeller(@RequestBody LoginReq loginReq) throws Exception {
        String otp = loginReq.getOtp();
        String email = loginReq.getEmail();
        loginReq.setEmail("seller_" + email);
        AuthRes res = authService.signin(loginReq);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody LoginOtpReq req) throws Exception {
        authService.sendOtpVerificationEmail(req.getEmail(), req.getRole());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("OTP sent successfully");
        return ResponseEntity.ok(apiResponse);
    }
}
