package com.itp.breathsafe.user.service;

import com.itp.breathsafe.user.dto.LoginRequestDTO;
import com.itp.breathsafe.user.dto.LoginResponseDTO;
import com.itp.breathsafe.user.entity.User;
import com.itp.breathsafe.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {
    @Autowired
    private UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    public UserAuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();

        LoginResponseDTO loginResponse = new LoginResponseDTO(user);
        loginResponse.setToken(jwtService.generateToken(user));
        return loginResponse;
    }


    // Create User
    public LoginResponseDTO createNewUser(@Valid LoginRequestDTO userDTO) {
        // Check if username already exists
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        // Create new user entity
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));


        // Save to DB
        User savedUser = userRepository.save(user);

        // Generate token after registration
        LoginResponseDTO loginResponse = new LoginResponseDTO(savedUser);
        loginResponse.setToken(jwtService.generateToken(savedUser));

        return loginResponse;

    }



}
