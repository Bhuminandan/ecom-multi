package com.zinum.service;

public interface EmailService {
    void sendOtpVerificationEmail(String email, String otp, String subject, String body);
}
