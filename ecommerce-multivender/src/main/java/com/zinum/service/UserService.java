package com.zinum.service;

import com.zinum.model.User;

public interface UserService {

    User findUserByJwtToken(String token);
    User findUserByEmail(String email);
}
