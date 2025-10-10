package com.itp.breathsafe.subscription.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.subscription.dto.*;
import com.itp.breathsafe.subscription.repository.SubscriptionRepository;
import com.itp.breathsafe.user.enums.Role;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionAdminService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionAdminService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    private void authorizeAdmin(Role role) {
        if (role != Role.ADMIN) {
            throw new CustomException("Unauthorized request!");
        }
    }

    public List<SubscriptionCountDTO> getSubscriptionCounts(Role role) {
        authorizeAdmin(role);
        // The query is now sorted by count descending in the repository
        return subscriptionRepository.getSubscriptionCountPerSensor();
    }

    public List<SubscriptionDetailAdminDTO> getAllSubscriptionDetails(Role role) {
        authorizeAdmin(role);
        return subscriptionRepository.findAllActiveWithDetails().stream()
                .map(sub -> new SubscriptionDetailAdminDTO(
                        sub.getId(),
                        sub.getAlertThreshold(),
                        sub.getEmailNotifications(),
                        sub.getCreatedAt(),
                        sub.getUser().getId(),
                        sub.getUser().getUsername(),
                        sub.getUser().getEmail(),
                        sub.getSensor().getId(),
                        sub.getSensor().getName(),
                        sub.getSensor().getLocation()
                ))
                .collect(Collectors.toList());
    }

    public List<TopUserDTO> getTopSubscribingUsers(Role role, int limit) {
        authorizeAdmin(role);
        // Use pagination to limit the results
        return subscriptionRepository.findTopSubscribingUsers(PageRequest.of(0, limit));
    }

    public NotificationStatsDTO getNotificationPreferenceStats(Role role) {
        authorizeAdmin(role);
        long enabled = subscriptionRepository.countByIsActiveTrueAndEmailNotificationsIsTrue();
        long disabled = subscriptionRepository.countByIsActiveTrueAndEmailNotificationsIsFalse();
        long total = subscriptionRepository.countByIsActiveTrue();
        return new NotificationStatsDTO(enabled, disabled, total);
    }
}