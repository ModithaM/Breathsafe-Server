package com.itp.breathsafe.data.repository;

import com.itp.breathsafe.data.dto.SensorDataDisplayDTO;
import com.itp.breathsafe.data.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    @Query("""
        SELECT new com.itp.breathsafe.data.dto.SensorDataDisplayDTO(
            s.id,
            s.name,
            s.location,
            s.latitude,
            s.longitude,
            s.status,
            s.createdAt,
            d.co2Level,
            d.aqiValue,
            CAST(d.timestamp AS string)
        )
        FROM Sensor s
        JOIN SensorData d ON d.sensor.id = s.id
        WHERE d.timestamp = (
            SELECT MAX(sd.timestamp)
            FROM SensorData sd
            WHERE sd.sensor.id = s.id
        )
        """)
    List<SensorDataDisplayDTO> getSensorsWithLatestData();
}
