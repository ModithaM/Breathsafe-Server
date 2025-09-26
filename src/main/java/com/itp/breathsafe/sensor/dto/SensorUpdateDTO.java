package com.itp.breathsafe.sensor.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorUpdateDTO extends SensorUpsertDTO {

    @NotNull(message = "last maintainance date is required")
    private LocalDateTime lastMaintenance;
}
