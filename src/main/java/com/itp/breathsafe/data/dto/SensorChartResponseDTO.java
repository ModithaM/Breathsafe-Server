package com.itp.breathsafe.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorChartResponseDTO {
    private SensorDetailsDTO sensorDetails;
    private List<SensorChartDTO> chartData;
    private int totalRecords;
}
