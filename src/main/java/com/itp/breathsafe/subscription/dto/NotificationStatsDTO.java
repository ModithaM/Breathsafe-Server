package com.itp.breathsafe.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStatsDTO {
    private long emailNotificationsEnabled;
    private long emailNotificationsDisabled;
    private long totalActiveSubscriptions;
}