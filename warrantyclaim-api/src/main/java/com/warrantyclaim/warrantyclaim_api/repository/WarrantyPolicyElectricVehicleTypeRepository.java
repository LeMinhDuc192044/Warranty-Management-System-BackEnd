package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicleType;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyPolicyElectricVehicleType;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyPolicyElectricVehicleTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyPolicyElectricVehicleTypeRepository extends JpaRepository<WarrantyPolicyElectricVehicleType, WarrantyPolicyElectricVehicleTypeId> {
    List<WarrantyPolicyElectricVehicleType> findByVehicleType(ElectricVehicleType vehicleType);
}
