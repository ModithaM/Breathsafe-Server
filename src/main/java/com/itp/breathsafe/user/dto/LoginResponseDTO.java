package com.itp.breathsafe.user.dto;

import com.itp.breathsafe.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String username;
    private String role;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;

    public LoginResponseDTO(User user){
        this.username = user.getUsername();
        this.role = user.getRole().name();
        this.userId = user.getId().toString();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }
}
