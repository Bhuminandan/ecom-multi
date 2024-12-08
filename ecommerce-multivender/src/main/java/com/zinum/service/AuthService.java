package com.zinum.service;

import com.zinum.dto.request.LoginReq;
import com.zinum.dto.request.SignupReq;
import com.zinum.dto.response.AuthRes;
import com.zinum.enums.UserRoles;

public interface AuthService {
    void sendOtpVerificationEmail(String email, UserRoles role);
    String createUser(SignupReq signupReq) throws Exception;
    AuthRes signin(LoginReq loginReq, UserRoles role) throws Exception;
}
