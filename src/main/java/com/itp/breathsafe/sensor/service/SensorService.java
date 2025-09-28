package com.itp.breathsafe.sensor.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.sensor.dto.SensorReadDTO;
import com.itp.breathsafe.sensor.dto.SensorUpdateDTO;
import com.itp.breathsafe.sensor.dto.SensorUpsertDTO;
import com.itp.breathsafe.sensor.entity.Sensor;
import com.itp.breathsafe.sensor.repository.SensorRepository;
import com.itp.breathsafe.user.enums.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public void createSensor(SensorUpsertDTO sensorUpsertDTO, Role role) {

        if (role != Role.ADMIN) {
            throw new CustomException("Unauthorized request!");
        }

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

    /**
     * Update Sensor Details
     *
     * @param sensorUpdateDTO updated details
     * @param id              sensor id
     * @param role            Updating users role
     */
    public void updateSensor(SensorUpdateDTO sensorUpdateDTO, long id, Role role) {

        if (role != Role.ADMIN) {
            throw new CustomException("Unauthorized request!");
        }

        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new CustomException("Sensor not found"));

        try {
            sensor.setInstallationDate(sensorUpdateDTO.getInstallationDate());
            sensor.setIsActive(sensorUpdateDTO.getIsActive());
            sensor.setLatitude(sensorUpdateDTO.getLatitude());
            sensor.setLocation(sensorUpdateDTO.getLocation());
            sensor.setLongitude(sensorUpdateDTO.getLongitude());
            sensor.setName(sensorUpdateDTO.getName());
            sensor.setLastMaintenance(sensorUpdateDTO.getLastMaintenance());
            sensor.setStatus(sensorUpdateDTO.getStatus());
            sensorRepository.save(sensor);
        } catch (Exception e) {
            throw new CustomException("Failed to update sensor details");
        }
    }

    public void deleteSensor(Long id, Role role) {

        if (role != Role.ADMIN) {
            throw new CustomException("Unauthorized request!");
        }

        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new CustomException("Sensor not found"));

        sensorRepository.delete(sensor);
    }

    public List<SensorReadDTO> getAllSensors() {
        List<Sensor> sensors = sensorRepository.findAll();

        return sensors.stream().map(sensor -> new SensorReadDTO(
                sensor.getId(),
                sensor.getName(),
                sensor.getLocation(),
                sensor.getLatitude(),
                sensor.getLongitude(),
                sensor.getStatus(),
                sensor.getInstallationDate(),
                sensor.getLastMaintenance(),
                sensor.getBatteryLevel(),
                sensor.getIsActive(),
                sensor.getCreatedAt(),
                sensor.getUpdatedAt()
        )).collect(Collectors.toList());
    }
}