package com.zinum.dto.request;

import com.zinum.enums.UserRoles;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginOtpReq {
    String email;
    String otp;
    UserRoles role;
}
