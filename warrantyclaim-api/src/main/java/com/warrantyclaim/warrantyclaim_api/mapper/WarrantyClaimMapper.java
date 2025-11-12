package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.*;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class WarrantyClaimMapper {

    public WarrantyClaimDetailResponseDTO toDetailResponse(WarrantyClaim claim) {
        if (claim == null)
            return null;

        WarrantyClaimDetailResponseDTO response = new WarrantyClaimDetailResponseDTO();
        response.setClaimId(claim.getId());
        response.setOfficeBranch(claim.getOfficeBranch());
        response.setCustomerName(claim.getCustomerName());
        response.setCustomerPhone(claim.getCustomerPhone());
        response.setClaimDate(claim.getClaimDate());
        response.setIssueDescription(claim.getIssueDescription());
        response.setEmail(claim.getEmail());
        response.setStatus(claim.getStatus());
        response.setRequiredPart(claim.getRequiredParts());

        // Map vehicle
        if (claim.getVehicle() != null) {
            response.setVehicle(toVehicleDetailInfo(claim.getVehicle()));
        }

        // Map staff
        if (claim.getStaff() != null) {
            response.setAssignedStaff(toStaffBasicInfoDTO(claim.getTechnician()));
        }

        // Map spare parts

        // Map work assignments

        return response;
    }

    // Mapper for Warranty Claim Create Request
    public WarrantyClaim toEntityWarrantyClaim(WarrantyClaimCreateRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        WarrantyClaim warrantyClaim = new WarrantyClaim();
        warrantyClaim.setCustomerName(requestDTO.getCustomerName());
        warrantyClaim.setCustomerPhone(requestDTO.getPhoneNumber());
        warrantyClaim.setOfficeBranch(requestDTO.getOfficeBranch());
        warrantyClaim.setClaimDate(requestDTO.getClaimDate());
        warrantyClaim.setIssueDescription(requestDTO.getIssueDescription());
        warrantyClaim.setEmail(requestDTO.getEmail());

        if (requestDTO.getRequiredPart() != null) {
            warrantyClaim.setRequiredParts(requestDTO.getRequiredPart());
        }

        // Vehicle and Staff relationships should be set in the service layer

        return warrantyClaim;
    }

    public WarrantyClaimResponseDTO toResponseWarrantyClaim(WarrantyClaim warrantyClaim) {
        if (warrantyClaim == null) {
            return null;
        }

        WarrantyClaimResponseDTO response = new WarrantyClaimResponseDTO();
        response.setClaimId(warrantyClaim.getId());
        response.setClaimDate(warrantyClaim.getClaimDate());
        response.setIssueDescription(warrantyClaim.getIssueDescription());
        response.setEmail(warrantyClaim.getEmail());
        response.setCustomerName(warrantyClaim.getCustomerName());
        response.setOfficeBranch(warrantyClaim.getOfficeBranch());
        response.setCustomerPhone(warrantyClaim.getCustomerPhone());
        response.setStatus(warrantyClaim.getStatus());
        response.setRequiredPart(warrantyClaim.getRequiredParts());
        response.setStatus(warrantyClaim.getStatus());

        // Map vehicle
        if (warrantyClaim.getVehicle() != null) {
            response.setVehicle(toVehicleBasicInfoDTO(warrantyClaim.getVehicle()));
        }
        //
        // // Map staff
        // if (warrantyClaim.getScStaff() != null) {
        // response.setAssignedStaff(toStaffBasicInfoDTO(warrantyClaim.getScStaff()));
        // }

        return response;
    }

    public WarrantyClaimListResponseDTO toListResponse(WarrantyClaim claim) {
        if (claim == null)
            return null;

        WarrantyClaimListResponseDTO response = new WarrantyClaimListResponseDTO();
        response.setClaimId(claim.getId());
        response.setCustomerName(claim.getCustomerName());
        response.setCustomerPhone(claim.getCustomerPhone());
        response.setClaimDate(claim.getClaimDate());
        response.setStatus(claim.getStatus()); // FIX: Thiếu set status
        response.setOfficeBranch(claim.getOfficeBranch());

        if (claim.getVehicle() != null) {
            response.setVehicleName(claim.getVehicle().getName());
        }

        return response;
    }

    public void updateEntity(WarrantyClaim claim, WarrantyClaimUpdateRequestDTO request) {
        if (request == null || claim == null)
            return;

        if (request.getCustomerName() != null) {
            claim.setCustomerName(request.getCustomerName());
        }
        if (request.getCustomerPhone() != null) {
            claim.setCustomerPhone(request.getCustomerPhone());
        }
        if (request.getIssueDescription() != null) {
            claim.setIssueDescription(request.getIssueDescription());
        }

        if (request.getStatus() != null) {
            claim.setStatus(request.getStatus());
        }

        if (request.getEmail() != null) {
            claim.setEmail(request.getEmail());
        }

        if (request.getClaimDate() != null) {
            claim.setClaimDate(request.getClaimDate());
        }

        if (request.getRequiredParts() != null) {
            claim.setRequiredParts(request.getRequiredParts());
        }

        if (request.getOfficeBranch() != null) {
            claim.setOfficeBranch(request.getOfficeBranch());
        }
        // Staff should be updated in service layer
    }

    // ------------------------------------------------------------------------------------------
    // Helper for info nested object

    private VehicleDetailInfo toVehicleDetailInfo(ElectricVehicle vehicle) {
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
            info.setVehicleTypeName(vehicle.getVehicleType().getModelName());
            info.setElectricVehicleType(vehicle.getVehicleType());
        }

        return info;
    }

    public VehicleBasicInfoDTO toVehicleBasicInfoDTO(ElectricVehicle vehicle) {
        if (vehicle == null)
            return null;

        VehicleBasicInfoDTO info = new VehicleBasicInfoDTO();
        info.setVehicleId(vehicle.getId());
        info.setVehicleName(vehicle.getName());
        info.setOwner(vehicle.getOwner());
        info.setPicture(vehicle.getPicture());
        info.setEmail(vehicle.getEmail());
        info.setPhoneNumber(vehicle.getPhoneNumber());

        if (vehicle.getVehicleType() != null) {
            info.setModel(vehicle.getVehicleType().getModelName());
        }

        return info;
    }

    public StaffBasicInfoDTO toStaffBasicInfoDTO(SCTechnician staff) {
        if (staff == null)
            return null;

        StaffBasicInfoDTO info = new StaffBasicInfoDTO();
        info.setStaffId(staff.getId());
        info.setAccountName(staff.getName());
        info.setEmail(staff.getEmail());
        info.setPhoneNumber(staff.getPhoneNumber());
        return info;
    }

}
