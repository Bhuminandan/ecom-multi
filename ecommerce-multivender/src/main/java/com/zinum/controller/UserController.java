package com.zinum.controller;

import com.zinum.model.User;
import com.zinum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String token) {
        User user = userService.findUserByJwtToken(token);
        return ResponseEntity.ok(user);
    }
}
