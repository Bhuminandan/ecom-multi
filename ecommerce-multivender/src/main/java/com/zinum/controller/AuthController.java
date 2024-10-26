package com.zinum.controller;

import com.zinum.dto.request.SignupReq;
import com.zinum.dto.response.AuthRes;
import com.zinum.enums.UserRoles;
import com.zinum.model.User;
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
    public ResponseEntity<AuthRes> createUserHandler(@RequestBody SignupReq signupReq) {
        String jwt = authService.createUser(signupReq);
        AuthRes res = new AuthRes();
        res.setJwt(jwt);
        res.setMessage("User created successfully");
        res.setRole(UserRoles.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }

}
