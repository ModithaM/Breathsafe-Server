package com.itp.breathsafe.request.controller;

import com.itp.breathsafe.request.dto.RequestUpsertDTO;
import com.itp.breathsafe.request.service.SensorRequestService;
import com.itp.breathsafe.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensorRequests")
public class SensorRequestController {

    private final SensorRequestService sensorRequestService;

    public SensorRequestController(SensorRequestService sensorRequestService) {
        this.sensorRequestService = sensorRequestService;
    }

    @PostMapping
    public ResponseEntity<?> createSensorRequest(
            @Valid @RequestBody RequestUpsertDTO requestUpsertDTO,
            @AuthenticationPrincipal User user
    ) {
        sensorRequestService.createSensorRequest(requestUpsertDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
