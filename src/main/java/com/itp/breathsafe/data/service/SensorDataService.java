package com.itp.breathsafe.data.service;

import com.itp.breathsafe.data.dto.DataUpsertDTO;
import com.itp.breathsafe.data.entity.SensorData;
import com.itp.breathsafe.data.enums.AQICategory;
import com.itp.breathsafe.data.repository.SensorDataRepository;
import com.itp.breathsafe.sensor.entity.Sensor;
import com.itp.breathsafe.sensor.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final SensorRepository sensorRepository;

    public SensorDataService(SensorDataRepository sensorDataRepository, SensorRepository sensorRepository) {
        this.sensorDataRepository = sensorDataRepository;
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public SensorData createSensorData(DataUpsertDTO dataUpsertDTO) {

        //Check sensor is in the database
        Optional<Sensor> sensorOpt = sensorRepository.findById(dataUpsertDTO.getSensorId());
        if(sensorOpt.isEmpty()) {
            throw new RuntimeException("Sensor with id " + dataUpsertDTO.getSensorId() + " not found");
        }
        Sensor sensor = sensorOpt.get();

        AQICategory category = findAQICategory(dataUpsertDTO.getAqiValue());

        SensorData sensorData = new SensorData();
        sensorData.setTemperature(dataUpsertDTO.getTemperature());
        sensorData.setHumidity(dataUpsertDTO.getHumidity());
        sensorData.setCo2Level(dataUpsertDTO.getCo2Level());
        sensorData.setAqiValue(dataUpsertDTO.getAqiValue());
        sensorData.setAqiCategory(category);
        sensorData.setSensor(sensor);

        return sensorDataRepository.save(sensorData);
    }

    //find AQI Category based on AQI value
    private AQICategory findAQICategory(Integer aqiValue) {
        if(aqiValue>=0 && aqiValue<=50) {
            return AQICategory.GOOD;
        } else if(aqiValue <= 100) {
            return AQICategory.MODERATE;
        } else if(aqiValue <= 150) {
            return AQICategory.UNHEALTHY_SENSITIVE;
        } else if(aqiValue <= 200) {
            return AQICategory.UNHEALTHY;
        } else if(aqiValue <= 300) {
            return AQICategory.VERY_UNHEALTHY;
        } else {
            return AQICategory.HAZARDOUS;
        }
    }
}