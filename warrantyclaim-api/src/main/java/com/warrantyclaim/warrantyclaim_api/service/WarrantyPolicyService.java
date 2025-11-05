package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyCreateDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyListDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyResponseDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPolicyUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WarrantyPolicyService {
    WarrantyPolicyResponseDTO createWarrantyPolicy(WarrantyPolicyCreateDTO createDTO);
    WarrantyPolicyResponseDTO getWarrantyPolicyById(String id);
    WarrantyPolicyResponseDTO updateWarrantyPolicy(String id, WarrantyPolicyUpdateDTO updateDTO);
    void deleteWarrantyPolicy(String id);
    Page<WarrantyPolicyListDTO> getAllWarrantyPolicies(Pageable pageable);

    // Vehicle Type Management
    WarrantyPolicyResponseDTO assignVehicleTypes(String policyId, List<String> vehicleTypeIds);
    public WarrantyPolicyResponseDTO addVehicleType(String policyId, String vehicleTypeId);
    WarrantyPolicyResponseDTO removeVehicleType(String policyId, String vehicleTypeId);

    // SC Spare Parts Type Management
    WarrantyPolicyResponseDTO assignSparePartsTypeSC(String policyId, List<String> sparePartsTypeSCIds);
    WarrantyPolicyResponseDTO addSparePartsTypeSC(String policyId, String sparePartsTypeId);
    WarrantyPolicyResponseDTO removeSparePartsTypeSC(String policyId, String sparePartsTypeId);

    // EVM Spare Parts Type Management
    WarrantyPolicyResponseDTO assignSparePartsTypeEVM(String policyId, List<String> sparePartsTypeEVMIds);
    WarrantyPolicyResponseDTO addSparePartsTypeEVM(String policyId, String sparePartsTypeId);
    WarrantyPolicyResponseDTO removeSparePartsTypeEVM(String policyId, String sparePartsTypeId);
}
