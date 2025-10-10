package com.itp.breathsafe.subscription.controller;

import com.itp.breathsafe.subscription.dto.AdminSubscriptionSummaryDTO;
import com.itp.breathsafe.subscription.dto.SensorSubscriptionCountDTO;
import com.itp.breathsafe.subscription.dto.TimeSeriesPointDTO;
import com.itp.breathsafe.subscription.service.AdminSubscriptionAnalyticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/subscriptions")
@PreAuthorize("hasRole('ADMIN')")
public class AdminSubscriptionAnalyticsController {

    private final AdminSubscriptionAnalyticsService service;

    public AdminSubscriptionAnalyticsController(AdminSubscriptionAnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/metrics/by-sensor")
    public List<SensorSubscriptionCountDTO> bySensor() {
        return service.getCountsBySensor();
    }

    @GetMapping("/metrics/by-day")
    public List<TimeSeriesPointDTO> byDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getDailySubscriptions(from, to);
    }

    @GetMapping("/metrics/summary")
    public AdminSubscriptionSummaryDTO summary() {
        return service.getSummary();
    }

    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> report(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        byte[] pdf = service.generatePdfReport(from, to);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(String.format("breathsafe_subscription_report_%s_to_%s.pdf", from, to))
                .build());
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}