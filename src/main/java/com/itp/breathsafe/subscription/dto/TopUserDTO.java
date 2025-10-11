package com.itp.breathsafe.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopUserDTO {
    private Long userId;
    private String username;
    private Long subscriptionCount;
}