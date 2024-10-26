package com.zinum.service;

import com.zinum.dto.request.SignupReq;

public interface AuthService {
    String createUser(SignupReq signupReq);
}
