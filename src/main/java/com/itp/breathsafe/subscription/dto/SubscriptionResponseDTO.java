package com.itp.breathsafe.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponseDTO {
    private Long id;

    private Integer alertThreshold;
    private Boolean emailNotifications;
    private Boolean isActive;

    private LocalDateTime createdAt;

    // Sensor info (flattened for FE convenience)
    private Long sensorId;
    private String sensorName;
    private String sensorLocation;
    private Double latitude;
    private Double longitude;
    private String sensorStatus;
}