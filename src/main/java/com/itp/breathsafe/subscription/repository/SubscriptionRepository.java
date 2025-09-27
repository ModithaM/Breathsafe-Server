package com.itp.breathsafe.subscription.repository;

import com.itp.breathsafe.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserIdAndIsActiveTrue(Long userId);
    boolean existsByUserIdAndSensorIdAndIsActiveTrue(Long userId, Long sensorId);
    Optional<Subscription> findByIdAndUserId(Long id, Long userId);
}