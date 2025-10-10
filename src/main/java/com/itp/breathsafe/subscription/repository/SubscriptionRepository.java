package com.itp.breathsafe.subscription.repository;

import com.itp.breathsafe.subscription.dto.SensorSubscriptionCountDTO;
import com.itp.breathsafe.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUserIdAndIsActiveTrue(Long userId);
    boolean existsByUserIdAndSensorIdAndIsActiveTrue(Long userId, Long sensorId);
    Optional<Subscription> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT new com.itp.breathsafe.subscription.dto.SensorSubscriptionCountDTO(s.id, s.name, COUNT(sub)) " +
            "FROM Subscription sub JOIN sub.sensor s " +
            "GROUP BY s.id, s.name " +
            "ORDER BY COUNT(sub) DESC")
    List<SensorSubscriptionCountDTO> countBySensor();

    // Returns [java.sql.Date, Long] tuples; service maps to DTO.
    @Query("SELECT function('date', sub.createdAt) as d, COUNT(sub) " +
            "FROM Subscription sub " +
            "WHERE sub.createdAt BETWEEN :from AND :to " +
            "GROUP BY function('date', sub.createdAt) " +
            "ORDER BY d ASC")
    List<Object[]> countByDay(@Param("from") LocalDate from, @Param("to") LocalDate to);
}