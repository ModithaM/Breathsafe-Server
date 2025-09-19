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

    /**
     * Create a new sensor installation request.
     *
     * @param requestUpsertDTO The DTO containing request details.
     * @param requestedBy      The user making the request.
     * @throws CustomException If there is an error during creation.
     */
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

    /**
     * Delete a sensor installation request.
     *
     * @param id   The ID of the request to delete.
     * @param user The user attempting to delete the request.
     * @throws CustomException If the request is not found, the user is not authorized, or the request is not pending.
     */
    @Transactional
    public void deleteSensorRequest(Long id, User user) {
        SensorInstallationRequest sensorRequest = sensorRequestRepository.findById(id)
                .orElseThrow(() -> new CustomException("Sensor request not found"));

        if (!sensorRequest.getRequester().getId().equals(user.getId())) {
            throw new CustomException("You are not authorized to delete this request");
        }

        if (sensorRequest.getStatus() != RequestStatus.PENDING) {
            throw new CustomException("Only pending requests can be deleted");
        }

        sensorRequestRepository.delete(sensorRequest);
    }
}
