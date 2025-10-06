package com.itp.breathsafe.data.dto;

import com.itp.breathsafe.data.enums.AQICategory;
import com.itp.breathsafe.sensor.enums.SensorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataDisplayDTO {
    private Long sensorId;
    private String sensorName;
    private String location;
    private Double latitude;
    private Double longitude;
    private SensorStatus sensorStatus;
    private LocalDateTime createdAt;
    private Long dataId;
    private Double co2Level;
    private Integer aqiValue;
    private AQICategory aqiCategory;
    private String timestamp;
}