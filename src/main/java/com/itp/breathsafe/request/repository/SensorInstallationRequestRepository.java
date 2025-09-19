package com.itp.breathsafe.request.repository;

import com.itp.breathsafe.request.entity.SensorInstallationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorInstallationRequestRepository extends JpaRepository<SensorInstallationRequest, Long> {
}
