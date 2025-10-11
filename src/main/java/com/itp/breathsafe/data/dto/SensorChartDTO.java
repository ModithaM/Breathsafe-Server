package com.itp.breathsafe.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorChartDTO {
    private LocalDateTime timestamp;
    private Double co2Level;
    private Integer aqiValue;
}
