package com.itp.breathsafe.sensor.controller;

import com.itp.breathsafe.sensor.dto.SensorUpsertDTO;
import com.itp.breathsafe.sensor.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/sensors")
@RestController
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping
    public ResponseEntity<Void> createSensor(
            @Valid @RequestBody SensorUpsertDTO sensorUpsertDTO
    ) {
        sensorService.createSensor(sensorUpsertDTO);
        return ResponseEntity.ok().build();
    }
}
