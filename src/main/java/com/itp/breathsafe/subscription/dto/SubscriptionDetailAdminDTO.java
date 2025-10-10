package com.itp.breathsafe.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDetailAdminDTO {
    // Subscription Info
    private Long subscriptionId;
    private Integer alertThreshold;
    private Boolean emailNotifications;
    private LocalDateTime subscribedAt;

    // User Info
    private Long userId;
    private String username;
    private String userEmail;

    // Sensor Info
    private Long sensorId;
    private String sensorName;
    private String sensorLocation;
}