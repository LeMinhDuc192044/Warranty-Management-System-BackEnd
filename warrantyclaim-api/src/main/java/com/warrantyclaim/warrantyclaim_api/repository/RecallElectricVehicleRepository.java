package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.RecallElectricVehicle;
import com.warrantyclaim.warrantyclaim_api.entity.RecallElectricVehicleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface RecallElectricVehicleRepository extends JpaRepository<RecallElectricVehicle, RecallElectricVehicleId> {
    List<RecallElectricVehicle> findByRecallId(String recallId);
    Optional<RecallElectricVehicle> findByRecallIdAndVehicleId(String recallId, String vehicleId);
}
