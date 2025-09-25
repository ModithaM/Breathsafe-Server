package com.itp.breathsafe.data.controller;

import com.itp.breathsafe.data.dto.DataUpsertDTO;
import com.itp.breathsafe.data.entity.SensorData;
import com.itp.breathsafe.data.service.SensorDataService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/publicData")
@Validated
public class PublicDataController {
    private final SensorDataService sensorDataService;

    public PublicDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @PostMapping
    public ResponseEntity<String> addSensorData(@Valid @RequestBody DataUpsertDTO dataUpsertDTO) {

        //call service to save data
        SensorData sensorData = sensorDataService.createSensorData(dataUpsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Data saved successfully with id: " + sensorData.getId());
    }
}