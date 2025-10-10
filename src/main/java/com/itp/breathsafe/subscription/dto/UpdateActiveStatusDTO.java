package com.itp.breathsafe.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateActiveStatusDTO {

    @NotNull(message = "The 'isActive' field is required.")
    private Boolean isActive;

}