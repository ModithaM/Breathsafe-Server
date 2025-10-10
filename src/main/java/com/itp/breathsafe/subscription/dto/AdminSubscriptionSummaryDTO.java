package com.itp.breathsafe.subscription.dto;

import java.util.List;

public class AdminSubscriptionSummaryDTO {
    private Long totalSubscriptions;
    private Integer totalSensors;
    private Integer sensorsWithSubscriptions;
    private List<String> sensorsWithoutSubscriptions;

    public AdminSubscriptionSummaryDTO(Long totalSubscriptions, Integer totalSensors,
                                       Integer sensorsWithSubscriptions, List<String> sensorsWithoutSubscriptions) {
        this.totalSubscriptions = totalSubscriptions;
        this.totalSensors = totalSensors;
        this.sensorsWithSubscriptions = sensorsWithSubscriptions;
        this.sensorsWithoutSubscriptions = sensorsWithoutSubscriptions;
    }

    public Long getTotalSubscriptions() { return totalSubscriptions; }
    public Integer getTotalSensors() { return totalSensors; }
    public Integer getSensorsWithSubscriptions() { return sensorsWithSubscriptions; }
    public List<String> getSensorsWithoutSubscriptions() { return sensorsWithoutSubscriptions; }
}