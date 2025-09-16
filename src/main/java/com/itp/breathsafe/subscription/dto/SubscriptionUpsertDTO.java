package com.itp.breathsafe.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionUpsertDTO {

    private Integer alertThreshold = 100;

    @NotNull(message = "Email notification preference is required")
    private Boolean emailNotifications;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Sensor ID is required")
    private Long sensorId;
}
