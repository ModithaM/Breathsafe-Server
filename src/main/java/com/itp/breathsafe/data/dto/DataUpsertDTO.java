package com.itp.breathsafe.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataUpsertDTO {

    private Double temperature;
    private Double humidity;
    private Double co2Level;
    private Integer aqiValue;
    private Long sensorId;
}