package com.itp.breathsafe.data.entity;

import com.itp.breathsafe.data.enums.AQICategory;
import com.itp.breathsafe.sensor.entity.Sensor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double humidity;

    @Column(name = "co2_level", nullable = false)
    private Double co2Level;

    @Column(name = "aqi_value", nullable = false)
    private Integer aqiValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "aqi_category", nullable = false)
    private AQICategory aqiCategory;

    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;
}