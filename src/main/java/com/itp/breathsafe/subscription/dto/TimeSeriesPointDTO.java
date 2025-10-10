package com.itp.breathsafe.subscription.dto;

import java.time.LocalDate;

public class TimeSeriesPointDTO {
    private LocalDate date;
    private Long count;

    public TimeSeriesPointDTO(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }

    public LocalDate getDate() { return date; }
    public Long getCount() { return count; }
}