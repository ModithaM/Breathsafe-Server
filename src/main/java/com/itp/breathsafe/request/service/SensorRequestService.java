package com.itp.breathsafe.request.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.request.dto.RequestUpsertDTO;
import com.itp.breathsafe.request.entity.SensorInstallationRequest;
import com.itp.breathsafe.request.enums.RequestStatus;
import com.itp.breathsafe.request.repository.SensorInstallationRequestRepository;
import com.itp.breathsafe.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SensorRequestService {
    private final SensorInstallationRequestRepository sensorRequestRepository;

    public SensorRequestService(SensorInstallationRequestRepository sensorRequestRepository) {
        this.sensorRequestRepository = sensorRequestRepository;
    }

    @Transactional
    public void createSensorRequest(RequestUpsertDTO requestUpsertDTO , User requestedBy) {
        try{
            SensorInstallationRequest sensorRequest = new SensorInstallationRequest();
            sensorRequest.setRequestedLocation(requestUpsertDTO.getRequestedLocation());
            sensorRequest.setLatitude(requestUpsertDTO.getLatitude());
            sensorRequest.setLongitude(requestUpsertDTO.getLongitude());
            sensorRequest.setJustification(requestUpsertDTO.getJustification());
            sensorRequest.setStatus(RequestStatus.PENDING);
            sensorRequest.setRequester(requestedBy);

            sensorRequestRepository.save(sensorRequest);
        } catch (Exception e){
            throw new CustomException("Failed to create sensor request", e);
        }
    }
}
