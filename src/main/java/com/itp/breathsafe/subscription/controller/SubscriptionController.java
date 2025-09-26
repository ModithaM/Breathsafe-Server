package com.itp.breathsafe.subscription.controller;

import com.itp.breathsafe.subscription.dto.SubscriptionCreateDTO;
import com.itp.breathsafe.subscription.dto.SubscriptionResponseDTO;
import com.itp.breathsafe.subscription.dto.SubscriptionUpdateDTO;
import com.itp.breathsafe.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<SubscriptionResponseDTO>> mySubscriptions(Principal principal) {
        return ResponseEntity.ok(subscriptionService.getMySubscriptions(principal));
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponseDTO> subscribe(
            Principal principal,
            @Valid @RequestBody SubscriptionCreateDTO dto
    ) {
        return ResponseEntity.ok(subscriptionService.subscribe(principal, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> update(
            Principal principal,
            @PathVariable Long id,
            @Valid @RequestBody SubscriptionUpdateDTO dto
    ) {
        return ResponseEntity.ok(subscriptionService.update(principal, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unsubscribe(Principal principal, @PathVariable Long id) {
        subscriptionService.unsubscribe(principal, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> check(Principal principal, @RequestParam Long sensorId) {
        return ResponseEntity.ok(subscriptionService.isSubscribed(principal, sensorId));
    }
}