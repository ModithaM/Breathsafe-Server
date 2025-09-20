package com.itp.breathsafe.request.dto;

import com.itp.breathsafe.request.entity.SensorInstallationRequest;
import com.itp.breathsafe.request.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private Long id;
    private String requestedLocation;
    private Double latitude;
    private Double longitude;
    private String justification;
    private RequestStatus status;
    private String adminComments;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long requesterId;
    private String requesterName;

    private Long approvedById;
    private String approvedByName;

    private Long sensorId;
    private String sensorName;

    public RequestDTO(SensorInstallationRequest sensorInstallationRequest) {
        this.id = sensorInstallationRequest.getId();
        this.requestedLocation = sensorInstallationRequest.getRequestedLocation();
        this.latitude = sensorInstallationRequest.getLatitude();
        this.longitude = sensorInstallationRequest.getLongitude();
        this.justification = sensorInstallationRequest.getJustification();
        this.status = sensorInstallationRequest.getStatus();
        this.adminComments = sensorInstallationRequest.getAdminComments();
        this.approvedAt = sensorInstallationRequest.getApprovedAt();
        this.rejectedAt = sensorInstallationRequest.getRejectedAt();
        this.createdAt = sensorInstallationRequest.getCreatedAt();
        this.updatedAt = sensorInstallationRequest.getUpdatedAt();

        this.requesterId = sensorInstallationRequest.getRequester().getId();
        this.requesterName = sensorInstallationRequest.getRequester().getFirstName();

        if (sensorInstallationRequest.getApprovedBy() != null) {
            this.approvedById = sensorInstallationRequest.getApprovedBy().getId();
            this.approvedByName = sensorInstallationRequest.getApprovedBy().getFirstName();
        }

        if (sensorInstallationRequest.getAssignedSensor() != null) {
            this.sensorId = sensorInstallationRequest.getAssignedSensor().getId();
            this.sensorName = sensorInstallationRequest.getAssignedSensor().getName();
        }
    }
}
