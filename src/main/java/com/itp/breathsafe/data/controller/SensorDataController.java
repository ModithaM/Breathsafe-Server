package com.itp.breathsafe.data.controller;

import com.itp.breathsafe.data.dto.SensorDataDisplayDTO;
import com.itp.breathsafe.data.service.SensorDataService;
import com.itp.breathsafe.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sensorData")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @GetMapping
    public ResponseEntity<List<SensorDataDisplayDTO>> getSensorsWithData(
            @AuthenticationPrincipal User user
            ) {
        List<SensorDataDisplayDTO> sensorData = sensorDataService.getSensorsWithLatestData(user);
        return ResponseEntity.ok(sensorData);
    }
}
