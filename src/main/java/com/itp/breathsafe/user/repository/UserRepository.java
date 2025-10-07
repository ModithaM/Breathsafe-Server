package com.itp.breathsafe.user.repository;

import com.itp.breathsafe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    // Find users by address (location) - case-insensitive partial match
    List<User> findByAddressContainingIgnoreCase(String address);

    // Find user by ID and address (location)
    Optional<User> findByIdAndAddressContainingIgnoreCase(Long id, String address);
}
