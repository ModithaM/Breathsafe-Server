package com.itp.breathsafe.common.smartalert;

public record SmartAlertRequest(
        Double temperature,
        Double humidity,
        Double co2Level,
        Integer aqiValue
) {}