package com.itp.breathsafe.subscription.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.sensor.entity.Sensor;
import com.itp.breathsafe.subscription.dto.SubscriptionUpsertDTO;
import com.itp.breathsafe.subscription.entity.Subscription;
import com.itp.breathsafe.subscription.repository.SubscriptionRepository;
import com.itp.breathsafe.user.entity.User;
import com.itp.breathsafe.user.repository.UserRepository;
import com.itp.breathsafe.sensor.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SensorRepository sensorRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository,
                               SensorRepository sensorRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.sensorRepository = sensorRepository;
    }

    /**
     * Create a new subscription for a user and sensor.
     *
     * @param dto  The DTO containing subscription details.
     * @param actor The currently authenticated user (used for authorization).
     * @throws CustomException If lookups fail or save fails.
     */
    @Transactional
    public void createSubscription(SubscriptionUpsertDTO dto, User actor) {
        try {
            // Basic ownership check (prevent creating a subscription for another user)
            if (!actor.getId().equals(dto.getUserId())) {
                throw new CustomException("You are not authorized to create a subscription for another user");
            }

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new CustomException("User not found"));

            Sensor sensor = sensorRepository.findById(dto.getSensorId())
                    .orElseThrow(() -> new CustomException("Sensor not found"));

            Subscription sub = new Subscription();
            sub.setAlertThreshold(dto.getAlertThreshold() != null ? dto.getAlertThreshold() : 100);
            sub.setEmailNotifications(dto.getEmailNotifications());
            sub.setIsActive(dto.getIsActive());
            sub.setUser(user);
            sub.setSensor(sensor);

            subscriptionRepository.save(sub);
        } catch (CustomException e) {
            throw e; // let known business errors propagate
        } catch (Exception e) {
            throw new CustomException("Failed to create subscription", e);
        }
    }

    /**
     * Delete a subscription.
     *
     * @param id    The subscription ID.
     * @param actor The currently authenticated user.
     * @throws CustomException If not found or not authorized.
     */
    @Transactional
    public void deleteSubscription(Long id, User actor) {
        Subscription sub = subscriptionRepository.findById(id)
                .orElseThrow(() -> new CustomException("Subscription not found"));

        // Only the owner can delete (adjust if admins should be allowed)
        if (!sub.getUser().getId().equals(actor.getId())) {
            throw new CustomException("You are not authorized to delete this subscription");
        }

        try {
            subscriptionRepository.delete(sub);
        } catch (Exception e) {
            throw new CustomException("Failed to delete subscription", e);
        }
    }
}
