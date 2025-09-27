package com.itp.breathsafe.request.service;

import com.itp.breathsafe.common.events.CreateRequestEvent;
import com.itp.breathsafe.common.events.UserRegisteredEvent;
import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.request.dto.RequestApproveDTO;
import com.itp.breathsafe.request.dto.RequestDTO;
import com.itp.breathsafe.request.dto.RequestUpsertDTO;
import com.itp.breathsafe.request.entity.SensorInstallationRequest;
import com.itp.breathsafe.request.enums.RequestStatus;
import com.itp.breathsafe.request.repository.SensorInstallationRequestRepository;
import com.itp.breathsafe.sensor.entity.Sensor;
import com.itp.breathsafe.sensor.repository.SensorRepository;
import com.itp.breathsafe.user.entity.User;
import com.itp.breathsafe.user.enums.Role;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorRequestService {
    private final SensorInstallationRequestRepository sensorRequestRepository;
    private final SensorRepository sensorRepository;
    private final ApplicationEventPublisher publisher;

    public SensorRequestService(SensorInstallationRequestRepository sensorRequestRepository, SensorRepository sensorRepository, ApplicationEventPublisher publisher) {
        this.sensorRequestRepository = sensorRequestRepository;
        this.sensorRepository = sensorRepository;
        this.publisher = publisher;
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
            publisher.publishEvent(new CreateRequestEvent(requestedBy.getEmail()));
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

    /**
     * Retrieve all sensor installation requests made by the logged-in user.
     *
     * @param user The logged-in user.
     * @return A list of RequestDTOs representing the user's sensor requests.
     */
    public List<RequestDTO> getLoggedInUserSensorRequests(User user) {
        return sensorRequestRepository.findByRequesterIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(RequestDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve all sensor installation requests
     *
     * @throws CustomException If the user is not authorized.
     * @return A list of RequestDTOs
     */
    public List<RequestDTO> getAllSensorRequests(User user) {

        if(user.getRole() != Role.ADMIN) {
            throw new CustomException("Unauthorized access!");
        }

        return sensorRequestRepository.findAll()
                .stream()
                .map(RequestDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Update a sensor installation request.
     *
     * @param requestUpsertDTO The DTO containing updated request details.
     * @param id               The ID of the request to update.
     * @param userId           The ID of the user attempting to update the request.
     * @throws CustomException If the request is not found, the user is not authorized, or the request is not pending.
     */
    @Transactional
    public void updateSensorRequest(RequestUpsertDTO requestUpsertDTO, Long id, Long userId) {
        SensorInstallationRequest sensorRequest = sensorRequestRepository.findById(id)
                .orElseThrow(() -> new CustomException("Sensor request not found"));

        if (!sensorRequest.getRequester().getId().equals(userId)) {
            throw new CustomException("You are not authorized to update this request");
        }

        if (sensorRequest.getStatus() != RequestStatus.PENDING) {
            throw new CustomException("Only pending requests can be updated");
        }

        sensorRequest.setRequestedLocation(requestUpsertDTO.getRequestedLocation());
        sensorRequest.setLatitude(requestUpsertDTO.getLatitude());
        sensorRequest.setLongitude(requestUpsertDTO.getLongitude());
        sensorRequest.setJustification(requestUpsertDTO.getJustification());

        sensorRequestRepository.save(sensorRequest);
    }

    /**
     * Approve a sensor installation request.
     *
     * @param requestId                The ID of the request to approve.
     * @param requestApproveDTO The DTO containing approval details.
     * @param user              The admin user approving the request.
     * @throws CustomException If the request is not found, the user is not authorized, the request is not pending, or the sensor is not found.
     */
    @Transactional
    public void approveSensorRequest(Long requestId, RequestApproveDTO requestApproveDTO, User user) {

        if(user.getRole() != Role.ADMIN) {
            throw new CustomException("Unauthorized access!");
        }

        SensorInstallationRequest sensorRequest = sensorRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException("Sensor request not found"));

        if (sensorRequest.getStatus() != RequestStatus.PENDING) {
            throw new CustomException("Only pending requests can be approved or rejected");
        }

        Sensor sensor = sensorRepository.findById(requestApproveDTO.getSensorId())
                .orElseThrow(() -> new CustomException("Sensor not found"));

        sensorRequest.setApprovedAt(LocalDateTime.now());
        sensorRequest.setStatus(RequestStatus.APPROVED);
        sensorRequest.setAdminComments(requestApproveDTO.getAdminComments());
        sensorRequest.setReviewedBy(user);
        sensorRequest.setAssignedSensor(sensor);
        sensorRequestRepository.save(sensorRequest);
    }

    /**
     * Reject a sensor installation request.
     *
     * @param requestId                The ID of the request to reject.
     * @param requestApproveDTO The DTO containing rejection details.
     * @param user              The admin user rejecting the request.
     * @throws CustomException If the request is not found, the user is not authorized, or the request is not pending.
     */
    @Transactional
    public void rejectSensorRequest(Long requestId, RequestApproveDTO requestApproveDTO, User user) {

        if(user.getRole() != Role.ADMIN) {
            throw new CustomException("Unauthorized access!");
        }

        SensorInstallationRequest sensorRequest = sensorRequestRepository.findById(requestId)
                .orElseThrow(() -> new CustomException("Sensor request not found"));

        if (sensorRequest.getStatus() != RequestStatus.PENDING) {
            throw new CustomException("Only pending requests can be approved or rejected");
        }

        sensorRequest.setRejectedAt(LocalDateTime.now());
        sensorRequest.setStatus(RequestStatus.REJECTED);
        sensorRequest.setAdminComments(requestApproveDTO.getAdminComments());
        sensorRequest.setReviewedBy(user);
        sensorRequestRepository.save(sensorRequest);
    }
}
