package com.itp.breathsafe.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDTO {
    private Long subscriptionId;
    private Integer alertThreshold;
    private Boolean emailNotifications;
    private Boolean isActive;
    private LocalDateTime subscribedAt;
    private Long sensorId;
    private String sensorName;
    private String sensorLocation;
}