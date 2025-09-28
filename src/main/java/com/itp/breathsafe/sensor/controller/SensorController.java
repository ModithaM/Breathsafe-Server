package com.itp.breathsafe.sensor.controller;

import com.itp.breathsafe.sensor.dto.SensorReadDTO;
import com.itp.breathsafe.sensor.dto.SensorUpdateDTO;
import com.itp.breathsafe.sensor.dto.SensorUpsertDTO;
import com.itp.breathsafe.sensor.service.SensorService;
import com.itp.breathsafe.user.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/sensors")
@RestController
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping
    public ResponseEntity<Void> createSensor(
            @Valid @RequestBody SensorUpsertDTO sensorUpsertDTO,
            @AuthenticationPrincipal User user
    ) {
        sensorService.createSensor(sensorUpsertDTO, user.getRole());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSensor(
            @Valid @RequestBody SensorUpdateDTO sensorUpdateDTO,
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        sensorService.updateSensor(sensorUpdateDTO, id, user.getRole());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensor(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ){
        sensorService.deleteSensor(id, user.getRole());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SensorReadDTO>> getAllSensors() {
        List<SensorReadDTO> sensors = sensorService.getAllSensors();
        return ResponseEntity.ok(sensors);
    }
}