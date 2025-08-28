package com.itp.breathsafe.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String token;
    private String username;
    private String role;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
