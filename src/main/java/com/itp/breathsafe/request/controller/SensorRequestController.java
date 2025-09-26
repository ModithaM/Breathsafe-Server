package com.itp.breathsafe.request.controller;

import com.itp.breathsafe.request.dto.RequestDTO;
import com.itp.breathsafe.request.dto.RequestUpsertDTO;
import com.itp.breathsafe.request.entity.SensorInstallationRequest;
import com.itp.breathsafe.request.service.SensorRequestService;
import com.itp.breathsafe.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sensorRequests")
public class SensorRequestController {

    private final SensorRequestService sensorRequestService;

    public SensorRequestController(SensorRequestService sensorRequestService) {
        this.sensorRequestService = sensorRequestService;
    }

    @PostMapping
    public ResponseEntity<Void> createSensorRequest(
            @Valid @RequestBody RequestUpsertDTO requestUpsertDTO,
            @AuthenticationPrincipal User user
    ) {
        sensorRequestService.createSensorRequest(requestUpsertDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        sensorRequestService.deleteSensorRequest(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/myRequests")
    public ResponseEntity<List<RequestDTO>> getLoggedInUserSensorRequests(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(sensorRequestService.getLoggedInUserSensorRequests(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSensorRequest(
            @Valid @RequestBody RequestUpsertDTO requestUpsertDTO,
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        sensorRequestService.updateSensorRequest(requestUpsertDTO, id, user.getId());
        return ResponseEntity.ok().build();
    }
}
