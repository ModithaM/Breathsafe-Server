package com.itp.breathsafe.user.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.user.dto.UserDTO;
import com.itp.breathsafe.user.entity.User;
import com.itp.breathsafe.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getCurrentUser(User user){
        UserDTO userDTO = new UserDTO();
         userDTO.setUsername(user.getUsername());
         userDTO.setEmail(user.getEmail());
         userDTO.setFirstName(user.getFirstName());
         userDTO.setLastName(user.getLastName());
         userDTO.setId(user.getId());
         userDTO.setPhone(user.getPhone());
         userDTO.setDateOfBirth(user.getDateOfBirth());
         userDTO.setAddress(user.getAddress());

         return userDTO;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail());
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    dto.setPhone(user.getPhone());
                    dto.setDateOfBirth(user.getDateOfBirth());
                    dto.setAddress(user.getAddress());
                    dto.setBio(user.getBio());
                    dto.setProfileImage(user.getProfileImage());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void updateCurrentUser(UserDTO user){
        User currentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException("User not found"));
        try {
            currentUser.setUsername(user.getUsername());
            currentUser.setEmail(user.getEmail());
            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
            currentUser.setPhone(user.getPhone());
            currentUser.setAddress(user.getAddress());
            currentUser.setDateOfBirth(user.getDateOfBirth());
            currentUser.setId(user.getId());

            userRepository.save(currentUser);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public void deleteCurrentUser(User user){
        userRepository.deleteById(user.getId());
    }
}
