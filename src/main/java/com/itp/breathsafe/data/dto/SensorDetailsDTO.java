package com.itp.breathsafe.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDetailsDTO {
    private Long sensorId;
    private String location;
    private String name;
}
