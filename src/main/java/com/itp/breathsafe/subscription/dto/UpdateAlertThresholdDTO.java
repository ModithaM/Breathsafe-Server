package com.itp.breathsafe.subscription.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAlertThresholdDTO {
    @NotNull(message = "Alert threshold cannot be null")
    @Min(value = 0, message = "Alert threshold must be at least 0")
    @Max(value = 1000, message = "Alert threshold must be at most 1000")
    private Integer alertThreshold;
}