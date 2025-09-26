package com.itp.breathsafe.data.controller;

import com.itp.breathsafe.data.service.SensorDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensorData")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @GetMapping
    public ResponseEntity<?> getLastTwelveRecords() {
        // Implementation for fetching the last twelve records
        return ResponseEntity.ok().build();
    }
}
