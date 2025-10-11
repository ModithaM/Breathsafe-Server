package com.itp.breathsafe.subscription.repository;

import com.itp.breathsafe.subscription.dto.SubscriptionCountDTO;
import com.itp.breathsafe.subscription.dto.TopUserDTO;
import com.itp.breathsafe.subscription.entity.Subscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    // ADMIN QUERIES (Existing)
    @Query("SELECT new com.itp.breathsafe.subscription.dto.SubscriptionCountDTO(s.sensor.id, s.sensor.name, COUNT(s.id)) " +
            "FROM Subscription s WHERE s.isActive = true GROUP BY s.sensor.id, s.sensor.name ORDER BY COUNT(s.id) DESC")
    List<SubscriptionCountDTO> getSubscriptionCountPerSensor();

    @Query("SELECT s FROM Subscription s JOIN FETCH s.user JOIN FETCH s.sensor WHERE s.isActive = true")
    List<Subscription> findAllActiveWithDetails();

    @Query("SELECT new com.itp.breathsafe.subscription.dto.TopUserDTO(s.user.id, s.user.username, COUNT(s.id)) " +
            "FROM Subscription s WHERE s.isActive = true GROUP BY s.user.id, s.user.username ORDER BY COUNT(s.id) DESC")
    List<TopUserDTO> findTopSubscribingUsers(Pageable pageable);

    long countByIsActiveTrueAndEmailNotificationsIsTrue();

    long countByIsActiveTrueAndEmailNotificationsIsFalse();

    long countByIsActiveTrue();

    // USER-SPECIFIC QUERIES (New)
    Optional<Subscription> findByUserIdAndSensorId(Long userId, Long sensorId);

    @Query("SELECT s FROM Subscription s JOIN FETCH s.sensor WHERE s.user.id = :userId")
    List<Subscription> findAllByUserIdWithSensorDetails(Long userId);

    Optional<Subscription> findByIdAndUserId(Long subscriptionId, Long userId);
}