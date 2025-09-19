package com.itp.breathsafe.user.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.user.dto.LoginRequestDTO;
import com.itp.breathsafe.user.dto.LoginResponseDTO;
import com.itp.breathsafe.user.dto.UserRegisterDTO;
import com.itp.breathsafe.user.entity.User;
import com.itp.breathsafe.user.enums.Role;
import com.itp.breathsafe.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserAuthService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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


    public void createNewUser(UserRegisterDTO userRegisterDTO) {

        if (userRepository.findByUsername(userRegisterDTO.getUsername()).isPresent()) {
            throw new CustomException("Username already exists!");
        }

        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setFirstName(userRegisterDTO.getFirstName());
        user.setLastName(userRegisterDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setRole(Role.valueOf(userRegisterDTO.getRole()));

        userRepository.save(user);
    }



}
