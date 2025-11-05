package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicle;
import com.warrantyclaim.warrantyclaim_api.entity.PartsRequest;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeEVM;
import com.warrantyclaim.warrantyclaim_api.enums.PartsRequestStatus;
import org.springframework.stereotype.Component;

@Component
public class PartsRequestMapper {
    public PartsRequest toEntity(PartsRequestCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        PartsRequest entity = new PartsRequest();
//        entity.setPartNumber(dto.getPartNumber());
        entity.setPartName(dto.getPartName());
        entity.setQuantity(dto.getQuantity());
        entity.setRequestDate(dto.getRequestDate());
        entity.setDeliveryDate(dto.getDeliveryDate());
        entity.setStatus(PartsRequestStatus.PENDING); // Default status
        entity.setRequestedByStaffId(dto.getRequestedByStaffId());
        entity.setBranchOffice(dto.getBranchOffice());

        // Note: partType and electricVehicle will be set in service layer

        return entity;
    }

    public VehicleBasicInfoDTO toVehicleBasicInfoDTO(ElectricVehicle vehicle) {
        if (vehicle == null) return null;
        VehicleBasicInfoDTO info = new VehicleBasicInfoDTO();
        info.setVehicleId(vehicle.getId());
        info.setVehicleName(vehicle.getName());
        info.setOwner(vehicle.getOwner());
        info.setEmail(vehicle.getEmail());
        info.setPicture(vehicle.getPicture());
        info.setPhoneNumber(vehicle.getPhoneNumber());

        if(vehicle.getVehicleType() != null) {
            info.setModel(vehicle.getVehicleType().getModelName());
        }
        return info;
    }

    /**
     * Convert Entity to Response DTO
     */
    public PartsRequestResponseDTO toResponseDTO(PartsRequest entity) {
        if (entity == null) {
            return null;
        }

        PartsRequestResponseDTO dto = new PartsRequestResponseDTO();
        dto.setId(entity.getId());
//        dto.setPartNumber(entity.getPartNumber());
        dto.setPartName(entity.getPartName());
        dto.setQuantity(entity.getQuantity());
        dto.setRequestDate(entity.getRequestDate());
        dto.setStatus(entity.getStatus());
        dto.setDeliveryDate(entity.getDeliveryDate());

        // Map nested objects
        if (entity.getPartType() != null) {
            dto.setPartType(toPartTypeInfo(entity.getPartType()));
        }

        if(entity.getElectricVehicle() != null) {
            dto.setVehicle(toVehicleBasicInfoDTO(entity.getElectricVehicle()));
        }

        return dto;
    }

    /**
     * Convert Entity to List DTO
     */
    public PartsResponseListDTO toListDTO(PartsRequest entity) {
        if (entity == null) {
            return null;
        }

        PartsResponseListDTO dto = new PartsResponseListDTO();
        dto.setId(entity.getId());
//        dto.setPartNumber(entity.getPartNumber());
        dto.setPartName(entity.getPartName());
        dto.setQuantity(entity.getQuantity());
        dto.setRequestDate(entity.getRequestDate());
        dto.setStatus(entity.getStatus());

        // Simplified nested info
        if (entity.getPartType() != null) {
            dto.setPartTypeName(entity.getPartType().getPartName());
        }

        if(entity.getElectricVehicle() != null) {
            dto.setVehicle(toVehicleBasicInfoDTO(entity.getElectricVehicle()));
        }

//        if (entity.getStaff() != null) {
//            dto.setStaffName(entity.getStaff().getAccountName());
//        }

        return dto;
    }

    /**
     * Update Entity from Update DTO
     */
    public void updateEntity(PartsRequest entity, PartsRequestUpdateDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

//        if (dto.getPartNumber() != null) {
//            entity.setPartNumber(dto.getPartNumber());
//        }

        if (dto.getPartName() != null) {
            entity.setPartName(dto.getPartName());
        }

        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }

        if (dto.getRequestDate() != null) {
            entity.setRequestDate(dto.getRequestDate());
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        if (dto.getDeliveryDate() != null) {
            entity.setDeliveryDate(dto.getDeliveryDate());
        }

        // Note: partType and staff will be updated in service layer
    }

    /**
     * Convert ProductsSparePartsTypeEVM to PartTypeInfoDTO
     */
    private PartTypeInfoDTO toPartTypeInfo(ProductsSparePartsTypeEVM partType) {
        if (partType == null) {
            return null;
        }

        PartTypeInfoDTO dto = new PartTypeInfoDTO();
        dto.setId(partType.getId());
        dto.setPartName(partType.getPartName());
        dto.setManufacturer(partType.getManufacturer());
        dto.setPrice(partType.getPrice());

        return dto;
    }
}
