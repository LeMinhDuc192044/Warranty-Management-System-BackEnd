package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.*;
import com.warrantyclaim.warrantyclaim_api.enums.CoverageTypeWarrantyPolicy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WarrantyPolicyMapper {

    // ============= CREATE/UPDATE Mappings =============

    /**
     * Convert WarrantyPolicyCreateDTO to WarrantyPolicy entity
     */
    public WarrantyPolicy toEntity(WarrantyPolicyCreateDTO createDTO) {
        if (createDTO == null) {
            return null;
        }

        WarrantyPolicy policy = new WarrantyPolicy();
        policy.setName(createDTO.getPolicyName());
        policy.setDescription(createDTO.getDescription());
        policy.setCoverageDurationMonths(createDTO.getCoverageDurationMonths());
        policy.setCoverageTypeWarrantyPolicy(createDTO.getCoverageType());

        return policy;
    }

    /**
     * Update existing WarrantyPolicy entity from WarrantyPolicyUpdateDTO
     */
    public void updateEntity(WarrantyPolicy policy, WarrantyPolicyUpdateDTO updateDTO) {
        if (policy == null || updateDTO == null) {
            return;
        }

        if (updateDTO.getPolicyName() != null) {
            policy.setName(updateDTO.getPolicyName());
        }
        if (updateDTO.getDescription() != null) {
            policy.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getCoverageDurationMonths() != null) {
            policy.setCoverageDurationMonths(updateDTO.getCoverageDurationMonths());
        }
        if (updateDTO.getCoverageType() != null) {
            policy.setCoverageTypeWarrantyPolicy(updateDTO.getCoverageType());
        }
    }

    // ============= Response Mappings =============

    /**
     * Convert WarrantyPolicy entity to WarrantyPolicyResponseDTO (full details)
     */
    public WarrantyPolicyResponseDTO toResponseDTO(WarrantyPolicy policy) {
        if (policy == null) {
            return null;
        }

        WarrantyPolicyResponseDTO dto = new WarrantyPolicyResponseDTO();
        dto.setId(policy.getId());
        dto.setPolicyName(policy.getName());
        dto.setDescription(policy.getDescription());
        dto.setCoverageDurationMonths(policy.getCoverageDurationMonths());
        dto.setCoverageType(policy.getCoverageTypeWarrantyPolicy());

        // Map covered vehicle types

        if(policy.getWarrantyPolicyElectricVehicleTypes() != null) {
            List<VehicleTypeInfoDTO> vehicleTypes = policy.getWarrantyPolicyElectricVehicleTypes().stream()
                    .map(wp -> toVehicleTypeInfoDTO(wp.getVehicleType()))
                    .collect(Collectors.toList());
            dto.setCoveredVehicleTypes(vehicleTypes);
        }

        if(policy.getWarrantyPolicyProductsSparePartsTypeSCs() != null) {
            List<SparePartsTypeSCInfoDTO> sparePartsTypeSC = policy.getWarrantyPolicyProductsSparePartsTypeSCs().stream()
                    .map(wp -> toSparePartsTypeSCInfoDTO(wp.getPartType()))
                    .collect(Collectors.toList());
            dto.setCoveredSparePartsTypeSC(sparePartsTypeSC);
        }

        if(policy.getWarrantyPoliciesEvmTypes() != null) {
            List<SparePartsTypeEVMInfoDTO> sparePartsTypeEVM = policy.getWarrantyPoliciesEvmTypes().stream()
                    .map(wp -> toSparePartsTypeEVMInfoDTO(wp.getPartType()))
                    .collect(Collectors.toList());
            dto.setCoveredSparePartsTypeEVM(sparePartsTypeEVM);
        }


        return dto;
    }

    public WarrantyPolicyListDTO toListDTO(WarrantyPolicy policy) {
        if (policy == null) {
            return null;
        }

        WarrantyPolicyListDTO dto = new WarrantyPolicyListDTO();
        dto.setId(policy.getId());
        dto.setPolicyName(policy.getName());
        dto.setDescription(policy.getDescription());
        dto.setCoverageDurationMonths(policy.getCoverageDurationMonths());
        dto.setCoverageType(policy.getCoverageTypeWarrantyPolicy());
        dto.setVehicleTypeCount(policy.getWarrantyPoliciesEvmTypes().size());
        dto.setSparePartsTypeSCCount(policy.getWarrantyPolicyProductsSparePartsTypeSCs().size());
        dto.setSparePartsTypeEVMCount(policy.getWarrantyPoliciesEvmTypes().size());

        return dto;
    }

    // ============= Related Entity Mappings =============

    /**
     * Convert ElectricVehicleType to VehicleTypeInfoDTO
     */
    public VehicleTypeInfoDTO toVehicleTypeInfoDTO(ElectricVehicleType vehicleType) {
        if (vehicleType == null) {
            return null;
        }

        VehicleTypeInfoDTO dto = new VehicleTypeInfoDTO();
        dto.setId(vehicleType.getId());
         //Adjust these based on your ElectricVehicleType entity fields
         dto.setBatteryType(vehicleType.getBatteryType());
         dto.setModelName(vehicleType.getModelName());
         dto.setPrice(vehicleType.getPrice());
         dto.setYearModelYear(vehicleType.getYearModelYear());
         dto.setDescription(vehicleType.getDescription());
        return dto;
    }

    /**
     * Convert ProductsSparePartsTypeSC to SparePartsTypeSCInfoDTO
     */
    public SparePartsTypeSCInfoDTO toSparePartsTypeSCInfoDTO(ProductsSparePartsTypeSC sparePartsType) {
        if (sparePartsType == null) {
            return null;
        }

        SparePartsTypeSCInfoDTO dto = new SparePartsTypeSCInfoDTO();
        dto.setId(sparePartsType.getId());
         dto.setPartName(sparePartsType.getPartName());
         dto.setManufacturer(sparePartsType.getManufacturer());
         dto.setYearModelYear(sparePartsType.getYearModelYear());
         dto.setPrice(sparePartsType.getPrice());

        return dto;
    }

    /**
     * Convert ProductsSparePartsTypeEVM to SparePartsTypeEVMInfoDTO
     */
    public SparePartsTypeEVMInfoDTO toSparePartsTypeEVMInfoDTO(ProductsSparePartsTypeEVM sparePartsType) {
        if (sparePartsType == null) {
            return null;
        }

        SparePartsTypeEVMInfoDTO dto = new SparePartsTypeEVMInfoDTO();
        dto.setId(sparePartsType.getId());
        dto.setPartName(sparePartsType.getPartName());
        dto.setManufacturer(sparePartsType.getManufacturer());
        dto.setYearModelYear(sparePartsType.getYearModelYear());
        dto.setPrice(sparePartsType.getPrice());

        return dto;
    }

    // ============= Utility Methods =============

    /**
     * Convert list of WarrantyPolicies to list of WarrantyPolicyListDTOs
     */
    public List<WarrantyPolicyListDTO> toListDTOList(List<WarrantyPolicy> policies) {
        if (policies == null) {
            return new ArrayList<>();
        }

        return policies.stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert list of WarrantyPolicies to list of WarrantyPolicyResponseDTOs
     */
    public List<WarrantyPolicyResponseDTO> toResponseDTOList(List<WarrantyPolicy> policies) {
        if (policies == null) {
            return new ArrayList<>();
        }

        return policies.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}