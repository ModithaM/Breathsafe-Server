package com.itp.breathsafe.subscription.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.sensor.entity.Sensor;
import com.itp.breathsafe.sensor.repository.SensorRepository;
import com.itp.breathsafe.subscription.dto.SubscriptionRequestDTO;
import com.itp.breathsafe.subscription.dto.SubscriptionResponseDTO;
import com.itp.breathsafe.subscription.entity.Subscription;
import com.itp.breathsafe.subscription.repository.SubscriptionRepository;
import com.itp.breathsafe.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionUserService {

    private final SubscriptionRepository subscriptionRepository;
    private final SensorRepository sensorRepository;

    public SubscriptionUserService(SubscriptionRepository subscriptionRepository, SensorRepository sensorRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public SubscriptionResponseDTO createSubscription(User user, SubscriptionRequestDTO requestDTO) {
        // Check for existing subscription
        subscriptionRepository.findByUserIdAndSensorId(user.getId(), requestDTO.getSensorId())
                .ifPresent(s -> {
                    throw new CustomException("You are already subscribed to this sensor.");
                });

        // Find the sensor
        Sensor sensor = sensorRepository.findById(requestDTO.getSensorId())
                .orElseThrow(() -> new CustomException("Sensor not found with the provided ID."));

        // Create and save the new subscription
        Subscription newSubscription = new Subscription();
        newSubscription.setUser(user);
        newSubscription.setSensor(sensor);
        Subscription savedSubscription = subscriptionRepository.save(newSubscription);

        return mapToResponseDTO(savedSubscription);
    }

    public List<SubscriptionResponseDTO> getUserSubscriptions(Long userId) {
        return subscriptionRepository.findAllByUserIdWithSensorDetails(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubscriptionResponseDTO updateAlertThreshold(Long userId, Long subscriptionId, int newThreshold) {
        Subscription subscription = findSubscriptionByIdAndUserId(subscriptionId, userId);
        subscription.setAlertThreshold(newThreshold);
        return mapToResponseDTO(subscriptionRepository.save(subscription));
    }

    @Transactional
    public SubscriptionResponseDTO updateEmailNotification(Long userId, Long subscriptionId, boolean isEnabled) {
        Subscription subscription = findSubscriptionByIdAndUserId(subscriptionId, userId);
        subscription.setEmailNotifications(isEnabled);
        return mapToResponseDTO(subscriptionRepository.save(subscription));
    }

    @Transactional
    public SubscriptionResponseDTO toggleSubscriptionActiveStatus(Long userId, Long subscriptionId, boolean isActive) {
        Subscription subscription = findSubscriptionByIdAndUserId(subscriptionId, userId);
        subscription.setIsActive(isActive);
        return mapToResponseDTO(subscriptionRepository.save(subscription));
    }

    @Transactional
    public void deleteSubscription(Long userId, Long subscriptionId) {
        Subscription subscription = findSubscriptionByIdAndUserId(subscriptionId, userId);
        subscriptionRepository.delete(subscription);
    }

    private Subscription findSubscriptionByIdAndUserId(Long subscriptionId, Long userId) {
        return subscriptionRepository.findByIdAndUserId(subscriptionId, userId)
                .orElseThrow(() -> new CustomException("Subscription not found or you do not have permission to access it."));
    }

    private SubscriptionResponseDTO mapToResponseDTO(Subscription sub) {
        return new SubscriptionResponseDTO(
                sub.getId(),
                sub.getAlertThreshold(),
                sub.getEmailNotifications(),
                sub.getIsActive(),
                sub.getCreatedAt(),
                sub.getSensor().getId(),
                sub.getSensor().getName(),
                sub.getSensor().getLocation()
        );
    }
}