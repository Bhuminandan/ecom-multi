package com.zinum.service;

import com.zinum.dto.request.LoginReq;
import com.zinum.dto.request.SignupReq;
import com.zinum.dto.response.AuthRes;

public interface AuthService {
    void sendOtpVerificationEmail(String email);
    String createUser(SignupReq signupReq) throws Exception;
    AuthRes signin(LoginReq loginReq) throws Exception;
}
