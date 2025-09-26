package com.itp.breathsafe.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestApproveDTO {
    private Long sensorId;
    private String adminComments;
}
