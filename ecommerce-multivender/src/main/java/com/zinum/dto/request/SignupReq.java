package com.zinum.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupReq {
    String email;
    String password;
    String firstName;
    String lastName;
    String mobile;
}
