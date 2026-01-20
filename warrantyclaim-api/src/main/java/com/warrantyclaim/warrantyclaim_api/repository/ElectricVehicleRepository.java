package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ElectricVehicleRepository extends JpaRepository<ElectricVehicle, String> {
    List<ElectricVehicle> findByVehicleType_Id(String vehicleTypeId);
    Page<ElectricVehicle> findByVehicleTypeModelNameIgnoreCase(String modelName, Pageable pageable);

    @Query("SELECT ev FROM ElectricVehicle ev where LOWER(ev.vehicleType.modelName) = LOWER(:modelName)")
    Page<ElectricVehicle> findByVehicleTypeModelNameIgnoreCaseQuery(@Param("modelName") String modelName, Pageable pageable);

}