package com.itp.breathsafe.user.controller;

import com.itp.breathsafe.user.dto.UserDTO;
import com.itp.breathsafe.user.entity.User;
import com.itp.breathsafe.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping
    public ResponseEntity<Void> updateCurrentUser(
            @RequestBody UserDTO userDTO
    ){
        userService.updateCurrentUser(userDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal User user){
        userService.deleteCurrentUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Get users by location
     * GET /api/users/location?address=Mumbai
     */
    @GetMapping("/location")
    public ResponseEntity<List<UserDTO>> getUsersByLocation(
            @RequestParam(name = "address") String location) {
        try {
            List<UserDTO> users = userService.getUsersByLocation(location);
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Get user by ID and location
     * GET /api/users/{id}/location?address=Mumbai
     */
    @GetMapping("/{id}/location")
    public ResponseEntity<UserDTO> getUserByIdAndLocation(
            @PathVariable Long id,
            @RequestParam(name = "address") String location) {
        try {
            UserDTO user = userService.getUserByIdAndLocation(id, location);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
