package com.zinum.controller;

import com.zinum.dto.request.LoginOtpReq;
import com.zinum.dto.request.LoginReq;
import com.zinum.dto.request.SignupReq;
import com.zinum.dto.response.ApiResponse;
import com.zinum.dto.response.AuthRes;
import com.zinum.enums.UserRoles;
import com.zinum.model.VerificationCode;
import com.zinum.repository.UserRepository;
import com.zinum.service.Impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthServiceImpl authService;

    public AuthController(UserRepository userRepository,
                          AuthServiceImpl authService)
    {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthRes> createUserHandler(@RequestBody SignupReq signupReq) throws Exception {
        String jwt = authService.createUser(signupReq);
        AuthRes res = new AuthRes();
        res.setJwt(jwt);
        res.setMessage("User created successfully");
        res.setRole(UserRoles.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/signing")
    public ResponseEntity<AuthRes> signinHandler(@RequestBody LoginReq loginReq) throws Exception {
        AuthRes res = authService.signin(loginReq, UserRoles.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }


    @PostMapping("/verification/send-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody LoginOtpReq req) throws Exception {
        authService.sendOtpVerificationEmail(req.getEmail(), req.getRole());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("OTP sent successfully");
        return ResponseEntity.ok(apiResponse);
    }

}
