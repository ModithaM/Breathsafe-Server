package com.itp.breathsafe.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateEmailNotificationDTO {
    @NotNull(message = "Email notification preference cannot be null")
    private Boolean enable;
}