package com.itp.breathsafe.subscription.controller;

import com.itp.breathsafe.subscription.dto.*;
import com.itp.breathsafe.subscription.service.SubscriptionAdminService;
import com.itp.breathsafe.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/subscriptions")
public class SubscriptionAdminController {

    private final SubscriptionAdminService subscriptionAdminService;

    public SubscriptionAdminController(SubscriptionAdminService subscriptionAdminService) {
        this.subscriptionAdminService = subscriptionAdminService;
    }

    @GetMapping("/counts")
    public ResponseEntity<List<SubscriptionCountDTO>> getSubscriptionCounts(
            @AuthenticationPrincipal User user
    ) {
        List<SubscriptionCountDTO> counts = subscriptionAdminService.getSubscriptionCounts(user.getRole());
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/details/all")
    public ResponseEntity<List<SubscriptionDetailAdminDTO>> getAllSubscriptionDetails(
            @AuthenticationPrincipal User user
    ) {
        List<SubscriptionDetailAdminDTO> details = subscriptionAdminService.getAllSubscriptionDetails(user.getRole());
        return ResponseEntity.ok(details);
    }

    @GetMapping("/trends/top-users")
    public ResponseEntity<List<TopUserDTO>> getTopSubscribingUsers(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<TopUserDTO> topUsers = subscriptionAdminService.getTopSubscribingUsers(user.getRole(), limit);
        return ResponseEntity.ok(topUsers);
    }

    @GetMapping("/trends/notification-stats")
    public ResponseEntity<NotificationStatsDTO> getNotificationPreferenceStats(
            @AuthenticationPrincipal User user
    ) {
        NotificationStatsDTO stats = subscriptionAdminService.getNotificationPreferenceStats(user.getRole());
        return ResponseEntity.ok(stats);
    }
}