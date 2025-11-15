package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicle;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicleType;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsSC;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeSC;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import org.springframework.stereotype.Component;

@Component
public class ElectricVehicleMapper {

    public ElectricVehicle toEntityElectricVehicle(VehicleCreateDTO vehicleCreateDTO) {
        if (vehicleCreateDTO == null) {
            return null;
        }

        ElectricVehicle electricVehicle = new ElectricVehicle();
        electricVehicle.setId(vehicleCreateDTO.getVehicleId());
        electricVehicle.setName(vehicleCreateDTO.getVehicleName());
        electricVehicle.setEmail(vehicleCreateDTO.getEmail());
        electricVehicle.setPurchaseDate(vehicleCreateDTO.getPurchaseDate());
        electricVehicle.setOwner(vehicleCreateDTO.getOwner());
        electricVehicle.setPhoneNumber(vehicleCreateDTO.getPhoneNumber());
        electricVehicle.setTotalKm(vehicleCreateDTO.getTotalKm());
        electricVehicle.setStatus(vehicleCreateDTO.getStatus());
        electricVehicle.setUsageType(vehicleCreateDTO.getUsageType()); // Map usage type


        return electricVehicle;
    }

    public ElectricVehicleListResponseDTO toListResponseDTO(ElectricVehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        ElectricVehicleListResponseDTO dto = new ElectricVehicleListResponseDTO();
        dto.setId(vehicle.getId());
        dto.setName(vehicle.getName());
        dto.setTotalKm(vehicle.getTotalKm());
        dto.setOwner(vehicle.getOwner());
        dto.setPhoneNumber(vehicle.getPhoneNumber());
        dto.setEmail(vehicle.getEmail());
        dto.setPurchaseDate(vehicle.getPurchaseDate());
        dto.setStatus(vehicle.getStatus());
        dto.setUsageType(vehicle.getUsageType()); // Add usage type to response
        dto.setPicture(vehicle.getPicture());
        dto.setReturnDate(vehicle.getReturnDate());
        dto.setVision(vehicle.getVersion());
        // Simplified vehicle type info
        if (vehicle.getVehicleType() != null) {
            dto.setVehicleTypeId(vehicle.getVehicleType().getId());
            dto.setModelName(vehicle.getVehicleType().getModelName());
        }

        if (vehicle.getProductsSparePartsSC() != null) {
            dto.setSparePartInfoScDTOList(
                    vehicle.getProductsSparePartsSC().stream()
                            .map(this::toResponseDto).toList());
        }

        return dto;
    }

    public ElectricVehicleResponseDTO toResponseDTO(ElectricVehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        ElectricVehicleResponseDTO dto = new ElectricVehicleResponseDTO();
        dto.setId(vehicle.getId());
        dto.setName(vehicle.getName());
        dto.setTotalKm(vehicle.getTotalKm());
        dto.setPicture(vehicle.getPicture());
        dto.setPurchaseDate(vehicle.getPurchaseDate());
        dto.setOwner(vehicle.getOwner());
        dto.setPhoneNumber(vehicle.getPhoneNumber());
        dto.setEmail(vehicle.getEmail());
        dto.setStatus(vehicle.getStatus());
        dto.setUsageType(vehicle.getUsageType()); // Add usage type to response
        dto.setReturnDate(vehicle.getReturnDate());

        // Map vehicle type
        if (vehicle.getVehicleType() != null) {
            dto.setVehicleType(toVehicleTypeInfo(vehicle.getVehicleType()));
        }

        if (vehicle.getProductsSparePartsSC() != null) {
            dto.setSparePartInfoScDTOList(
                    vehicle.getProductsSparePartsSC().stream()
                            .map(this::toResponseDto).toList());
        }

        return dto;
    }

    private VehicleTypeInfoDTO toVehicleTypeInfo(ElectricVehicleType vehicleType) {
        if (vehicleType == null) {
            return null;
        }

        VehicleTypeInfoDTO dto = new VehicleTypeInfoDTO();
        dto.setId(vehicleType.getId());
        dto.setDescription(vehicleType.getDescription());
        dto.setModelName(vehicleType.getModelName());
        dto.setYearModelYear(vehicleType.getYearModelYear());
        dto.setBatteryType(vehicleType.getBatteryType());
        dto.setPrice(vehicleType.getPrice());

        return dto;
    }

    public void updateEntityElectricVehicle(ElectricVehicleUpdateRequestDTO updatedVehicle,
            ElectricVehicle electricVehicle) {
        if (updatedVehicle.getName() != null) {
            electricVehicle.setName(updatedVehicle.getName());
        }

        if (updatedVehicle.getEmail() != null) {
            electricVehicle.setEmail(updatedVehicle.getEmail());
        }

        if (updatedVehicle.getOwner() != null) {
            electricVehicle.setOwner(updatedVehicle.getOwner());
        }

        if (updatedVehicle.getPhoneNumber() != null) {
            electricVehicle.setPhoneNumber(updatedVehicle.getPhoneNumber());
        }

        if (updatedVehicle.getTotalKm() != null) {
            electricVehicle.setTotalKm(updatedVehicle.getTotalKm());
        }

        if (updatedVehicle.getStatus() != null) {
            electricVehicle.setStatus(updatedVehicle.getStatus());
        }

        if (updatedVehicle.getUsageType() != null) {
            electricVehicle.setUsageType(updatedVehicle.getUsageType());
        }

        if (updatedVehicle.getPurchaseDate() != null) {
            electricVehicle.setPurchaseDate(updatedVehicle.getPurchaseDate());
        }

        if (updatedVehicle.getReturnDate() != null) {
            if (!updatedVehicle.getReturnDate().isAfter(updatedVehicle.getPurchaseDate())) {
                throw new IllegalArgumentException("Return date must be after purchase date!");
            }
            electricVehicle.setReturnDate(updatedVehicle.getReturnDate());
        }
        // Type Electric should be in vehicle service
    }

    public VehicleDetailInfo toVehicleDetailInfo(ElectricVehicle vehicle) {
        if (vehicle == null)
            return null;

        VehicleDetailInfo info = new VehicleDetailInfo();
        info.setVehicleId(vehicle.getId());
        info.setVehicleName(vehicle.getName());
        info.setTotalKm(vehicle.getTotalKm());
        info.setOwner(vehicle.getOwner());
        info.setPhoneNumber(vehicle.getPhoneNumber());
        info.setEmail(vehicle.getEmail());
        info.setStatus(vehicle.getStatus());
        info.setUsageType(vehicle.getUsageType());
        info.setPurchaseDate(vehicle.getPurchaseDate());

        if (vehicle.getVehicleType() != null) {
            info.setModelName(vehicle.getVehicleType().getModelName());
            info.setElectricVehicleType(vehicle.getVehicleType());
            info.setVehicleTypeName(vehicle.getVehicleType().getModelName());
        }

        return info;
    }

    public SparePartInfoScDTO toResponseDto(ProductsSparePartsSC entity) {
        if (entity == null)
            return null;

        SparePartInfoScDTO dto = new SparePartInfoScDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setVehicleType(entity.getVehicleType());
        dto.setCondition(entity.getCondition());
        dto.setOfficeBranch(entity.getOfficeBranch());

        return dto;
    }

}
