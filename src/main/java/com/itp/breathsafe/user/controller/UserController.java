package com.itp.breathsafe.user.controller;

import com.itp.breathsafe.user.dto.UserDTO;
import com.itp.breathsafe.user.entity.User;
import com.itp.breathsafe.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser(
            @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok(userService.getCurrentUser(user));
    }
}
