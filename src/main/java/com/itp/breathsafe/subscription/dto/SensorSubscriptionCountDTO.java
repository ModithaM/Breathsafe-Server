package com.itp.breathsafe.subscription.dto;

public class SensorSubscriptionCountDTO {
    private Long sensorId;
    private String sensorName;
    private Long subscriptionCount;

    public SensorSubscriptionCountDTO(Long sensorId, String sensorName, Long subscriptionCount) {
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.subscriptionCount = subscriptionCount;
    }

    public Long getSensorId() { return sensorId; }
    public String getSensorName() { return sensorName; }
    public Long getSubscriptionCount() { return subscriptionCount; }
}