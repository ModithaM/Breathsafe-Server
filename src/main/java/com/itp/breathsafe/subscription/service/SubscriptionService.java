package com.itp.breathsafe.subscription.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.sensor.entity.Sensor;
import com.itp.breathsafe.sensor.repository.SensorRepository;
import com.itp.breathsafe.subscription.dto.SubscriptionCreateDTO;
import com.itp.breathsafe.subscription.dto.SubscriptionResponseDTO;
import com.itp.breathsafe.subscription.dto.SubscriptionUpdateDTO;
import com.itp.breathsafe.subscription.entity.Subscription;
import com.itp.breathsafe.subscription.repository.SubscriptionRepository;
import com.itp.breathsafe.user.entity.User;
import com.itp.breathsafe.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SensorRepository sensorRepository;
    private final UserRepository userRepository;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            SensorRepository sensorRepository,
            UserRepository userRepository
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.sensorRepository = sensorRepository;
        this.userRepository = userRepository;
    }

    // CHANGED: use findByUsername instead of findByEmail
    private User requireCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new CustomException("Authenticated user not found"));
    }

    private SubscriptionResponseDTO toDTO(Subscription s) {
        return SubscriptionResponseDTO.builder()
                .id(s.getId())
                .alertThreshold(s.getAlertThreshold())
                .emailNotifications(s.getEmailNotifications())
                .isActive(s.getIsActive())
                .createdAt(s.getCreatedAt())
                .sensorId(s.getSensor().getId())
                .sensorName(s.getSensor().getName())
                .sensorLocation(s.getSensor().getLocation())
                .latitude(s.getSensor().getLatitude())
                .longitude(s.getSensor().getLongitude())
                .sensorStatus(s.getSensor().getStatus() != null ? s.getSensor().getStatus().name() : null)
                .build();
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponseDTO> getMySubscriptions(Principal principal) {
        User user = requireCurrentUser(principal);
        return subscriptionRepository.findByUserIdAndIsActiveTrue(user.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubscriptionResponseDTO subscribe(Principal principal, SubscriptionCreateDTO dto) {
        User user = requireCurrentUser(principal);

        Sensor sensor = sensorRepository.findById(dto.getSensorId())
                .orElseThrow(() -> new CustomException("Sensor not found with id: " + dto.getSensorId()));

        if (subscriptionRepository.existsByUserIdAndSensorIdAndIsActiveTrue(user.getId(), sensor.getId())) {
            throw new CustomException("Already subscribed to this sensor");
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSensor(sensor);
        subscription.setIsActive(true);
        subscription.setEmailNotifications(dto.getEmailNotifications() != null ? dto.getEmailNotifications() : true);
        subscription.setAlertThreshold(dto.getAlertThreshold() != null ? dto.getAlertThreshold() : 100);

        Subscription saved = subscriptionRepository.save(subscription);
        return toDTO(saved);
    }

    @Transactional
    public SubscriptionResponseDTO update(Principal principal, Long id, SubscriptionUpdateDTO dto) {
        User user = requireCurrentUser(principal);
        Subscription sub = subscriptionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new CustomException("Subscription not found"));

        if (dto.getAlertThreshold() != null) {
            sub.setAlertThreshold(dto.getAlertThreshold());
        }
        if (dto.getEmailNotifications() != null) {
            sub.setEmailNotifications(dto.getEmailNotifications());
        }
        if (dto.getIsActive() != null) {
            sub.setIsActive(dto.getIsActive());
        }

        return toDTO(subscriptionRepository.save(sub));
    }

    @Transactional
    public void unsubscribe(Principal principal, Long id) {
        User user = requireCurrentUser(principal);
        Subscription sub = subscriptionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new CustomException("Subscription not found"));

        // Soft delete
        sub.setIsActive(false);
        subscriptionRepository.save(sub);
    }

    @Transactional(readOnly = true)
    public boolean isSubscribed(Principal principal, Long sensorId) {
        User user = requireCurrentUser(principal);
        return subscriptionRepository.existsByUserIdAndSensorIdAndIsActiveTrue(user.getId(), sensorId);
    }
}
