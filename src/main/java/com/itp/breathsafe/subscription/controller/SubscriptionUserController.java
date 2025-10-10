package com.itp.breathsafe.subscription.controller;

import com.itp.breathsafe.subscription.dto.*;
import com.itp.breathsafe.subscription.service.SubscriptionUserService;
import com.itp.breathsafe.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionUserController {

    private final SubscriptionUserService subscriptionUserService;

    public SubscriptionUserController(SubscriptionUserService subscriptionUserService) {
        this.subscriptionUserService = subscriptionUserService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> subscribeToSensor(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody SubscriptionRequestDTO requestDTO
    ) {
        SubscriptionResponseDTO newSubscription = subscriptionUserService.createSubscription(user, requestDTO);
        return new ResponseEntity<>(newSubscription, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> getMySubscriptions(
            @AuthenticationPrincipal User user
    ) {
        List<SubscriptionResponseDTO> subscriptions = subscriptionUserService.getUserSubscriptions(user.getId());
        return ResponseEntity.ok(subscriptions);
    }

    @PatchMapping("/{id}/alert-threshold")
    public ResponseEntity<SubscriptionResponseDTO> updateAlertThreshold(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long subscriptionId,
            @Valid @RequestBody UpdateAlertThresholdDTO dto
    ) {
        SubscriptionResponseDTO updated = subscriptionUserService.updateAlertThreshold(user.getId(), subscriptionId, dto.getAlertThreshold());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/email-notifications")
    public ResponseEntity<SubscriptionResponseDTO> updateEmailNotifications(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long subscriptionId,
            @Valid @RequestBody UpdateEmailNotificationDTO dto
    ) {
        SubscriptionResponseDTO updated = subscriptionUserService.updateEmailNotification(user.getId(), subscriptionId, dto.getEnable());
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<SubscriptionResponseDTO> toggleSubscriptionStatus(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long subscriptionId,
            @Valid @RequestBody UpdateActiveStatusDTO dto
    ) {
        boolean isActive = dto.getIsActive();

        SubscriptionResponseDTO updatedSubscription = subscriptionUserService.toggleSubscriptionActiveStatus(user.getId(), subscriptionId, isActive);
        return ResponseEntity.ok(updatedSubscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unsubscribe(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Long subscriptionId
    ) {
        subscriptionUserService.deleteSubscription(user.getId(), subscriptionId);
        return ResponseEntity.noContent().build();
    }
}