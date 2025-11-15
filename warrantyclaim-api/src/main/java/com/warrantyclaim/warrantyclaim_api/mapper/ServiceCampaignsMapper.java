package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicleType;
import com.warrantyclaim.warrantyclaim_api.entity.Report;
import com.warrantyclaim.warrantyclaim_api.entity.SCTechnician;
import com.warrantyclaim.warrantyclaim_api.entity.ServiceCampaigns;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ServiceCampaignsMapper {

    public ServiceCampaigns toEntityServiceCampaigns(ServiceCampaignsRequestDTO requestDTO) {
        if(requestDTO == null) {
            return null;
        }

        ServiceCampaigns serviceCampaigns = new ServiceCampaigns();
        serviceCampaigns.setStartDate(requestDTO.getStartDate());
        serviceCampaigns.setEndDate(requestDTO.getEndDate());
        serviceCampaigns.setTypeName(requestDTO.getTypeName());
        serviceCampaigns.setRequiredParts(requestDTO.getRequiredParts());
        serviceCampaigns.setNotificationSent(requestDTO.getNotificationSent() != null ? requestDTO.getNotificationSent() : false);
        serviceCampaigns.setStatus(requestDTO.getStatus());
        serviceCampaigns.setDescription(requestDTO.getDescription());
        serviceCampaigns.setSpecialty(requestDTO.getSpecialty());
        // Note: relationships will be set in service layer

        return serviceCampaigns;
    }

    public ServiceCampaignsResponseDTO toResponseDTO(ServiceCampaigns serviceCampaigns) {
        if(serviceCampaigns == null) {
            return null;
        }
        ServiceCampaignsResponseDTO responseDTO = new ServiceCampaignsResponseDTO();

        responseDTO.setId(serviceCampaigns.getId());
        responseDTO.setSpecialty(serviceCampaigns.getSpecialty());
        responseDTO.setStartDate(serviceCampaigns.getStartDate());
        responseDTO.setEndDate(serviceCampaigns.getEndDate());
        responseDTO.setTypeName(serviceCampaigns.getTypeName());
        responseDTO.setSpecialty(serviceCampaigns.getSpecialty());
        responseDTO.setRequiredParts(serviceCampaigns.getRequiredParts());
        responseDTO.setNotificationSent(responseDTO.getNotificationSent() != null ? serviceCampaigns.getNotificationSent() : false);
        responseDTO.setStatus(serviceCampaigns.getStatus());
        responseDTO.setDescription(serviceCampaigns.getDescription());

        if (serviceCampaigns.getVehicleTypeCampaigns() != null) {
            responseDTO.setVehicleTypes(
                    serviceCampaigns.getVehicleTypeCampaigns().stream()
                            .map(junction -> toVehicleTypeBasic(junction.getVehicleType()))
                            .collect(Collectors.toList())
            );
        }

        // Map technicians through junction entity
        if (serviceCampaigns.getTechnicianCampaigns() != null) {
            responseDTO.setTechnicians(
                    serviceCampaigns.getTechnicianCampaigns().stream()
                            .map(junction -> toTechnicianBasic(junction.getTechnician()))
                            .collect(Collectors.toList())
            );
        }

        return responseDTO;
    }

    public ReportInfoListDTO toListReportDTO(ServiceCampaigns serviceCampaigns) {
        if(serviceCampaigns == null) {
            return null;
        }

        ReportInfoListDTO reportInfoListDTO = new ReportInfoListDTO();

        if(serviceCampaigns.getReports() != null) {
            reportInfoListDTO.setReportInfoDTOList(
                    serviceCampaigns.getReports().stream()
                            .map(this::toReportDTO).toList()
            );
        }
        return reportInfoListDTO;
    }

    public ReportInfoDTO toReportDTO(Report report) {
        ReportInfoDTO reportListInfoDTO = new ReportInfoDTO();
        reportListInfoDTO.setReportId(report.getId());
        reportListInfoDTO.setReportName(report.getReportName());
        reportListInfoDTO.setDescription(report.getDescription());
        reportListInfoDTO.setError(report.getError());
        return reportListInfoDTO;
    }

    public ServiceCampaignsListDTO toListDTO(ServiceCampaigns entity) {
        if (entity == null) {
            return null;
        }

        ServiceCampaignsListDTO dto = new ServiceCampaignsListDTO();
        dto.setCampaignsId(entity.getId());
        dto.setSpecialty(entity.getSpecialty());
        dto.setCampaignsTypeName(entity.getTypeName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setStatus(entity.getStatus());
        dto.setNotificationSent(entity.getNotificationSent());

        // Count relationships
        dto.setVehicleTypeCount(entity.getVehicleTypeCampaigns() != null
                ? entity.getVehicleTypeCampaigns().size()
                : 0);

        dto.setTechnicianCount(entity.getTechnicianCampaigns() != null
                ? entity.getTechnicianCampaigns().size()
                : 0);

        return dto;
    }

    public void updateEntity(ServiceCampaigns entity, ServiceCampaignsUpdateDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getCampaignsTypeName() != null) {
            entity.setTypeName(dto.getCampaignsTypeName());
        }

        if (dto.getStartDate() != null) {
            entity.setStartDate(dto.getStartDate());
        }

        if (dto.getEndDate() != null) {
            entity.setEndDate(dto.getEndDate());
        }

        if(dto.getSpecialty() != null) {
            entity.setSpecialty(dto.getSpecialty());
        }

        if (dto.getRequiredParts() != null) {
            entity.setRequiredParts(dto.getRequiredParts());
        }

        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        if (dto.getNotificationSent() != null) {
            entity.setNotificationSent(dto.getNotificationSent());
        }

        // Note: relationships will be updated in service layer
    }



    private TechnicianBasicDTO toTechnicianBasic(SCTechnician technician) {
        if (technician == null) {
            return null;
        }

        TechnicianBasicDTO dto = new TechnicianBasicDTO();
        dto.setId(technician.getId());
        dto.setName(technician.getName());
        dto.setEmail(technician.getEmail());
        dto.setSpecialty(technician.getSpecialty());

        return dto;
    }

    private VehicleTypeInfoDTO toVehicleTypeBasic(ElectricVehicleType vehicleType) {
        if (vehicleType == null) {
            return null;
        }

        VehicleTypeInfoDTO dto = new VehicleTypeInfoDTO();
        dto.setId(vehicleType.getId());
        dto.setModelName(vehicleType.getModelName());
        dto.setDescription(vehicleType.getDescription());
        dto.setYearModelYear(vehicleType.getYearModelYear());
        dto.setBatteryType(vehicleType.getBatteryType());
        dto.setPrice(vehicleType.getPrice());

        return dto;
    }



}
