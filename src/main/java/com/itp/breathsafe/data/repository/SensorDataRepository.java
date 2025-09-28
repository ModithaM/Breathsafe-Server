package com.itp.breathsafe.data.repository;

import com.itp.breathsafe.data.dto.SensorDataDisplayDTO;
import com.itp.breathsafe.data.entity.SensorData;
import com.itp.breathsafe.data.enums.AQICategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
            d.id,
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

    void deleteAllDataById(Long dataId);

    //partial update
    @Modifying
    @Query("""
        UPDATE SensorData d 
        SET d.aqiValue = :aqiValue, 
            d.aqiCategory = :aqiCategory, 
            d.co2Level = :co2Level 
        WHERE d.id = :dataId
        """)
    int updateAqiAndCo2Levels(
            @Param("dataId") Long dataId,
            @Param("aqiValue") Integer aqiValue,
            @Param("aqiCategory") AQICategory aqiCategory,
            @Param("co2Level") Integer co2Level
    );

}