package com.itp.breathsafe.sensor.dto;

import com.itp.breathsafe.sensor.enums.SensorStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorUpsertDTO {

    @NotNull(message = "Installation date is required")
    private LocalDateTime installationDate;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotBlank(message = "Sensor name is required")
    private String name;

    @NotNull(message = "Status is required")
    private SensorStatus status;
}
