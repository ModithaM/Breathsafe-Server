package com.itp.breathsafe.subscription.controller;

import com.itp.breathsafe.subscription.dto.SubscriptionUpsertDTO;
import com.itp.breathsafe.subscription.service.SubscriptionService;
import com.itp.breathsafe.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Create a new subscription
     */
    @PostMapping
    public ResponseEntity<Void> createSubscription(
            @Valid @RequestBody SubscriptionUpsertDTO subscriptionDTO,
            @AuthenticationPrincipal User user
    ) {
        subscriptionService.createSubscription(subscriptionDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Delete a subscription by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        subscriptionService.deleteSubscription(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
