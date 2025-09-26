package com.itp.breathsafe.sensor.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.sensor.dto.SensorUpsertDTO;
import com.itp.breathsafe.sensor.entity.Sensor;
import com.itp.breathsafe.sensor.repository.SensorRepository;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    /**
     * Create New Sensor
     *
     * @param sensorUpsertDTO sensor Upsert DTO
     */
    public void createSensor(SensorUpsertDTO sensorUpsertDTO) {
        try {
            Sensor sensor = new Sensor();

            sensor.setInstallationDate(sensorUpsertDTO.getInstallationDate());
            sensor.setIsActive(sensorUpsertDTO.getIsActive());
            sensor.setLatitude(sensorUpsertDTO.getLatitude());
            sensor.setLocation(sensorUpsertDTO.getLocation());
            sensor.setLongitude(sensorUpsertDTO.getLongitude());
            sensor.setName(sensorUpsertDTO.getName());
            sensor.setStatus(sensorUpsertDTO.getStatus());

            sensorRepository.save(sensor);
        } catch (Exception e) {
            throw new CustomException("Failed to create sensor details", e);
        }
    }
}
