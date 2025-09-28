package com.itp.breathsafe.sensor.dto;

import com.itp.breathsafe.sensor.enums.SensorStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorReadDTO {

    private Long id;
    private String name;
    private String location;
    private Double latitude;
    private Double longitude;
    private SensorStatus status;
    private LocalDateTime installationDate;
    private LocalDateTime lastMaintenance;
    private Integer batteryLevel;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
