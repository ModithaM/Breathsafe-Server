package com.itp.breathsafe.sensor.repository;

import com.itp.breathsafe.data.dto.SensorDataDisplayDTO;
import com.itp.breathsafe.sensor.dto.SensorReadDTO;
import com.itp.breathsafe.sensor.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {



}
