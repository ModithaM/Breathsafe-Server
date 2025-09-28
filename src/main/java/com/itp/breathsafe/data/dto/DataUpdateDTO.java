package com.itp.breathsafe.data.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataUpdateDTO {

    @NotNull(message = "Data ID cannot be null")
    private Long dataId;

    @Positive(message = "CO2 level must be a positive value")
    @Max(value = 5000, message = "CO2 level must not exceed 5000 ppm")
    private Integer co2Level;

    @Positive(message = "AQI value must be a positive value")
    @Max(value = 500, message = "AQI value must not exceed 500")
    private Integer aqiValue;
}