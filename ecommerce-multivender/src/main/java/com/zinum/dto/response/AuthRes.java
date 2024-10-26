package com.zinum.dto.response;

import com.zinum.enums.UserRoles;
import lombok.Data;

@Data
public class AuthRes {
    private String jwt;
    private String message;
    private UserRoles role;
}
