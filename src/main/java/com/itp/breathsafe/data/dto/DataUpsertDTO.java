package com.itp.breathsafe.data.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataUpsertDTO {

    @Min(value = -50, message = "Temperature must be above -50°C")
    @Max(value = 100, message = "Temperature must be below 100°C")
    private Double temperature;

    @Min(value = 0, message = "Humidity must be at least 0%")
    @Max(value = 100, message = "Humidity cannot exceed 100%")
    private Double humidity;

    @NotNull(message = "CO2 level is required")
    @Positive(message = "CO2 level must be a positive value")
    private Double co2Level;

    @NotNull(message = "AQI value is required")
    @Min(value = 0, message = "AQI value must be at least 0")
    @Max(value = 500, message = "AQI value cannot exceed 500")
    private Integer aqiValue;

    @NotNull(message = "Sensor ID is required")
    private Long sensorId;
}
