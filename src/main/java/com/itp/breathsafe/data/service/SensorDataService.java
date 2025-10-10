package com.itp.breathsafe.data.service;

import com.itp.breathsafe.common.exception.CustomException;
import com.itp.breathsafe.data.dto.*;
import com.itp.breathsafe.data.entity.SensorData;
import com.itp.breathsafe.data.enums.AQICategory;
import com.itp.breathsafe.data.repository.SensorDataRepository;
import com.itp.breathsafe.sensor.entity.Sensor;
import com.itp.breathsafe.sensor.repository.SensorRepository;
import com.itp.breathsafe.user.entity.User;
import com.itp.breathsafe.user.enums.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final SensorRepository sensorRepository;

    public SensorDataService(SensorDataRepository sensorDataRepository, SensorRepository sensorRepository) {
        this.sensorDataRepository = sensorDataRepository;
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public void createSensorData(DataUpsertDTO dataUpsertDTO) {

        //Check sensor is in the database
        Sensor sensor = sensorRepository.findById(dataUpsertDTO.getSensorId())
                .orElseThrow(() -> new CustomException("Sensor not found with id: " + dataUpsertDTO.getSensorId()));

        AQICategory category = findAQICategory(dataUpsertDTO.getAqiValue());

        SensorData sensorData = new SensorData();
        sensorData.setTemperature(dataUpsertDTO.getTemperature());
        sensorData.setHumidity(dataUpsertDTO.getHumidity());
        sensorData.setCo2Level(dataUpsertDTO.getCo2Level());
        sensorData.setAqiValue(dataUpsertDTO.getAqiValue());
        sensorData.setAqiCategory(category);
        sensorData.setSensor(sensor);

        sensorDataRepository.save(sensorData);
    }

    /** Get all sensors with their latest data
     * Only accessible by ADMIN users
     *
     * @param user the authenticated user
     * @return List of SensorDataDisplayDTO
     * @throws CustomException if user is not ADMIN
     */
    public List<SensorDataDisplayDTO> getSensorsWithLatestData(User user) {
        if(user.getRole() != Role.ADMIN) {
            throw new CustomException("Unauthorized access");
        }

        return  sensorDataRepository.getSensorsWithLatestData();
    }

    /** Delete all data entries associated with a specific data ID
     * Only accessible by ADMIN users
     *
     * @param user the authenticated user
     * @param dataId the ID of the data to delete
     * @throws CustomException if user is not ADMIN
     */
    @Transactional
    public void deleteAllDataById(User user, Long dataId) {
        if(user.getRole() != Role.ADMIN) {
            throw new CustomException("Unauthorized access");
        }

        sensorDataRepository.deleteAllDataById(dataId);
    }


    /** Update AQI and CO2 levels for a specific data entry
     * Only accessible by ADMIN users
     *
     * @param dataUpdateDTO the data update details
     * @param user the authenticated user
     * @return updated SensorDataDisplayDTO
     * @throws CustomException if user is not ADMIN or if update fails
     */
    @Transactional
    public SensorDataDisplayDTO updateSensorData(DataUpdateDTO dataUpdateDTO, User user) {
        if(user.getRole() != Role.ADMIN) {
            throw new CustomException("Unauthorized access");
        }

        // Check if the record exists
        sensorDataRepository.findById(dataUpdateDTO.getDataId())
                .orElseThrow(() -> new CustomException("Sensor data not found"));

        AQICategory category = findAQICategory(dataUpdateDTO.getAqiValue());

        int updatedRows = sensorDataRepository.updateAqiAndCo2Levels(
                dataUpdateDTO.getDataId(),
                dataUpdateDTO.getAqiValue(),
                category,
                dataUpdateDTO.getCo2Level()
        );

        if(updatedRows == 0) {
            throw new CustomException("Update failed");
        }

        SensorData updatedData = sensorDataRepository.findById(dataUpdateDTO.getDataId())
                .orElseThrow(() -> new CustomException("Sensor data not found after update"));

        return new SensorDataDisplayDTO(
                updatedData.getSensor().getId(),
                updatedData.getSensor().getName(),
                updatedData.getSensor().getLocation(),
                updatedData.getSensor().getLatitude(),
                updatedData.getSensor().getLongitude(),
                updatedData.getSensor().getStatus(),
                updatedData.getSensor().getCreatedAt(),
                updatedData.getId(),
                updatedData.getCo2Level(),
                updatedData.getAqiValue(),
                updatedData.getAqiCategory(),
                updatedData.getTimestamp().toString()
        );
    }

    /**
     * Get sensor chart data for the last 7 days - PUBLIC ACCESS
     * No authentication required, anyone can view sensor data
     *
     * @param sensorId the sensor ID
     * @return SensorChartResponseDTO with sensor details and chart data
     * @throws CustomException if sensor not found
     */
    public SensorChartResponseDTO getSensorChartData(Long sensorId) {
        //get sensor details
        SensorDetailsDTO sensorDetails = sensorDataRepository.findSensorDetails(sensorId)
                .orElseThrow(() -> new CustomException("Sensor data not found"));

        //calculate 7  previous days from now
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        //get chart data to 7 days
        List<SensorChartDTO> chartData = sensorDataRepository.findSensorChartData(sensorId, sevenDaysAgo);

        SensorChartResponseDTO response = new SensorChartResponseDTO();
        response.setChartData(chartData);
        response.setSensorDetails(sensorDetails);
        response.setTotalRecords(chartData.size());

        return response;
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