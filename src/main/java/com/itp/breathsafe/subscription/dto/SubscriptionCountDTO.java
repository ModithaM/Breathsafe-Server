package com.itp.breathsafe.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCountDTO {

    private Long sensorId;
    private String sensorName;
    private Long subscriberCount;

}