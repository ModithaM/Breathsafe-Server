package com.itp.breathsafe.subscription.dto;

import lombok.Data;

@Data
public class SubscriptionUpdateDTO {
    private Integer alertThreshold;
    private Boolean emailNotifications;
    private Boolean isActive; // allow soft-unsubscribe via patch as well
}