package com.itp.breathsafe.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionCreateDTO {
    @NotNull
    private Long sensorId;

    // Optional overrides
    private Integer alertThreshold; // default 100
    private Boolean emailNotifications; // default true
}