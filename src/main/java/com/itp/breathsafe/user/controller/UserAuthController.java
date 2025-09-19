package com.itp.breathsafe.user.controller;

import com.itp.breathsafe.user.dto.LoginRequestDTO;
import com.itp.breathsafe.user.dto.LoginResponseDTO;
import com.itp.breathsafe.user.dto.UserRegisterDTO;
import com.itp.breathsafe.user.service.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        LoginResponseDTO response =
                userAuthService.authenticateUser(loginRequestDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody UserRegisterDTO userRegisterDTO
    ) {
        userAuthService.createNewUser(userRegisterDTO);
        return ResponseEntity.ok().build();
    }

}
