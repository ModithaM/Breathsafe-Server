package com.itp.breathsafe.subscription.service;

import com.itp.breathsafe.subscription.dto.AdminSubscriptionSummaryDTO;
import com.itp.breathsafe.subscription.dto.SensorSubscriptionCountDTO;
import com.itp.breathsafe.subscription.dto.TimeSeriesPointDTO;
import com.itp.breathsafe.subscription.repository.SubscriptionRepository;
import com.itp.breathsafe.sensor.entity.Sensor;
import com.itp.breathsafe.sensor.repository.SensorRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminSubscriptionAnalyticsService {

    private final SubscriptionRepository subscriptionRepository;
    private final SensorRepository sensorRepository;

    public AdminSubscriptionAnalyticsService(SubscriptionRepository subscriptionRepository,
                                             SensorRepository sensorRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.sensorRepository = sensorRepository;
    }

    public List<SensorSubscriptionCountDTO> getCountsBySensor() {
        return subscriptionRepository.countBySensor();
    }

    public List<TimeSeriesPointDTO> getDailySubscriptions(LocalDate from, LocalDate to) {
        List<Object[]> rows = subscriptionRepository.countByDay(from, to);
        Map<LocalDate, Long> data = new LinkedHashMap<>();
        // Fill all days with zero first to ensure continuous series
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            data.put(d, 0L);
        }
        for (Object[] r : rows) {
            java.sql.Date sqlDate = (java.sql.Date) r[0];
            Long count = ((Number) r[1]).longValue();
            LocalDate day = sqlDate.toLocalDate();
            data.put(day, count);
        }
        return data.entrySet().stream()
                .map(e -> new TimeSeriesPointDTO(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public AdminSubscriptionSummaryDTO getSummary() {
        long totalSubscriptions = subscriptionRepository.count();

        List<Sensor> allSensors = sensorRepository.findAll();
        List<SensorSubscriptionCountDTO> counts = getCountsBySensor();

        Set<Long> sensorsWith = counts.stream()
                .map(SensorSubscriptionCountDTO::getSensorId)
                .collect(Collectors.toSet());

        List<String> sensorsWithout = allSensors.stream()
                .filter(s -> !sensorsWith.contains(s.getId()))
                .map(Sensor::getName)
                .sorted()
                .collect(Collectors.toList());

        return new AdminSubscriptionSummaryDTO(
                totalSubscriptions,
                allSensors.size(),
                sensorsWith.size(),
                sensorsWithout
        );
    }

    public byte[] generatePdfReport(LocalDate from, LocalDate to) {
        List<SensorSubscriptionCountDTO> bySensor = getCountsBySensor();
        List<TimeSeriesPointDTO> byDay = getDailySubscriptions(from, to);
        AdminSubscriptionSummaryDTO summary = getSummary();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font h1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font h2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 11);

            Paragraph title = new Paragraph("BreathSafe - Subscription Analytics Report", h1);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph period = new Paragraph(String.format("Period: %s to %s", from, to), normal);
            period.setSpacingBefore(8f);
            period.setSpacingAfter(12f);
            period.setAlignment(Element.ALIGN_CENTER);
            document.add(period);

            // Summary
            document.add(new Paragraph("Summary", h2));
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidths(new int[]{1, 2});
            summaryTable.setWidthPercentage(100);

            addRow(summaryTable, "Total Subscriptions", String.valueOf(summary.getTotalSubscriptions()), normal);
            addRow(summaryTable, "Total Sensors", String.valueOf(summary.getTotalSensors()), normal);
            addRow(summaryTable, "Sensors With Subscriptions", String.valueOf(summary.getSensorsWithSubscriptions()), normal);
            addRow(summaryTable, "Sensors Without Subscriptions", String.valueOf(summary.getSensorsWithoutSubscriptions().size()), normal);

            document.add(summaryTable);

            if (!CollectionUtils.isEmpty(summary.getSensorsWithoutSubscriptions())) {
                Paragraph none = new Paragraph("Sensors Without Subscriptions:", normal);
                none.setSpacingBefore(6f);
                document.add(none);
                com.lowagie.text.List ul = new com.lowagie.text.List(false, 12);
                for (String s : summary.getSensorsWithoutSubscriptions()) {
                    ul.add(new ListItem(s, normal));
                }
                document.add(ul);
            }

            document.add(Chunk.NEWLINE);

            // By Sensor
            document.add(new Paragraph("Subscriptions by Sensor", h2));
            PdfPTable sensorTable = new PdfPTable(3);
            sensorTable.setWidthPercentage(100);
            sensorTable.setSpacingBefore(5f);
            sensorTable.setWidths(new int[]{2, 6, 2});
            addTableHeader(sensorTable, new String[]{"Sensor ID", "Sensor Name", "Subscriptions"});
            for (SensorSubscriptionCountDTO row : bySensor) {
                sensorTable.addCell(new PdfPCell(new Phrase(String.valueOf(row.getSensorId()), normal)));
                sensorTable.addCell(new PdfPCell(new Phrase(row.getSensorName(), normal)));
                sensorTable.addCell(new PdfPCell(new Phrase(String.valueOf(row.getSubscriptionCount()), normal)));
            }
            document.add(sensorTable);

            document.add(Chunk.NEWLINE);

            // By Day
            document.add(new Paragraph("Daily Subscriptions", h2));
            PdfPTable dayTable = new PdfPTable(2);
            dayTable.setWidthPercentage(60);
            dayTable.setWidths(new int[]{3, 2});
            addTableHeader(dayTable, new String[]{"Date", "New Subscriptions"});
            for (TimeSeriesPointDTO p : byDay) {
                dayTable.addCell(new PdfPCell(new Phrase(p.getDate().toString(), normal)));
                dayTable.addCell(new PdfPCell(new Phrase(String.valueOf(p.getCount()), normal)));
            }
            document.add(dayTable);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed generating PDF", e);
        }
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private void addRow(PdfPTable table, String label, String value, Font font) {
        table.addCell(new PdfPCell(new Phrase(label, font)));
        table.addCell(new PdfPCell(new Phrase(value, font)));
    }
}