package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RecallMapper {

    public Recall toEntityRecall(RecallCreateDTO createDTO) {
        if(createDTO == null) {
            return null;
        }

        Recall recallEntity = new Recall();
        recallEntity.setName(createDTO.getName());
        recallEntity.setStatus(createDTO.getStatus());
        recallEntity.setIssueDescription(createDTO.getIssueDescription());
        recallEntity.setPartsRequired(createDTO.getPartsRequired());
        recallEntity.setRequiredAction(createDTO.getRequiredAction());
        recallEntity.setEvmApprovalStatus(createDTO.getEvmApprovalStatus());
        recallEntity.setNotificationSent(false);
        recallEntity.setSpecialty(createDTO.getSpecialty());
        recallEntity.setStartDate(createDTO.getStartDate());
        // Relationship in service layer

        return recallEntity;
    }

    public RecallResponseDTO toRecallResponseDTO (Recall recall) {
        if(recall == null) {
            return null;
        }

        RecallResponseDTO responseDTO = new RecallResponseDTO();
        responseDTO.setId(recall.getId());
        responseDTO.setSpecialty(recall.getSpecialty());
        responseDTO.setName(recall.getName());
        responseDTO.setStartDate(recall.getStartDate());
        responseDTO.setStatus(recall.getStatus());
        responseDTO.setNotificationSent(recall.getNotificationSent());
        responseDTO.setIssueDescription(recall.getIssueDescription());
        responseDTO.setPartsRequired(recall.getPartsRequired());
        responseDTO.setRequiredAction(recall.getRequiredAction());
        responseDTO.setEvmApprovalStatus(recall.getEvmApprovalStatus());

        if(recall.getRecallElectricVehicles() != null) {
            responseDTO.setVehicleBasicInfoDTOS(
                    recall.getRecallElectricVehicles().stream()
                            .map(junction -> toVehicleBasicInfoDTO(junction.getVehicle()))
                            .collect(Collectors.toList())
            );
        }

        if(recall.getTechniciansRecall() != null) {
            responseDTO.setTechnicianBasicDTOS(
                    recall.getTechniciansRecall().stream()
                            .map(junction -> toTechnicianBasic(junction.getTechnician()))
                            .collect(Collectors.toList())
            );
        }

        if(recall.getVehicleTypeRecalls() != null) {
            responseDTO.setVehicleTypeInfoDTOS(
                    recall.getVehicleTypeRecalls().stream()
                            .map(junction -> toVehicleTypeBasic(junction.getVehicleType()))
                            .collect(Collectors.toList())
            );
        }

        return responseDTO;
    }

    public ReportInfoListDTO toListReportResponseDTO(Recall recall) {
        if(recall == null) {
            return null;
        }

        ReportInfoListDTO reportInfoListDTO = new ReportInfoListDTO();

        if(recall.getReports() != null) {
            reportInfoListDTO.setReportInfoDTOList(
                    recall.getReports().stream()
                            .map(this::toReportDTO).toList()
            );
        }

        return  reportInfoListDTO;
    }

    public RecallsListDTO toListDTO(Recall recall) {
        if(recall == null) {
            return null;
        }

        RecallsListDTO recallsListDTO = new RecallsListDTO();
        recallsListDTO.setId(recall.getId());
        recallsListDTO.setName(recall.getName());
        recallsListDTO.setStatus(recall.getStatus());
        recallsListDTO.setPartsRequired(recall.getPartsRequired());
        recallsListDTO.setIssueDescription(recall.getIssueDescription());
        recallsListDTO.setRequiredAction(recall.getRequiredAction());
        recallsListDTO.setNotificationSent(recall.getNotificationSent());
        recallsListDTO.setStartDate(recall.getStartDate());
        recallsListDTO.setSpecialty(recall.getSpecialty());
        recallsListDTO.setEvmApprovalStatus(recall.getEvmApprovalStatus());

        recallsListDTO.setVehicleTypeCount(recall.getVehicleTypeRecalls() != null
                ? recall.getVehicleTypeRecalls().size() : 0
        );

        recallsListDTO.setVehicleCount(recall.getRecallElectricVehicles() != null
                ? recall.getRecallElectricVehicles().size() : 0
        );

        recallsListDTO.setTechnicianCount(recall.getRecallElectricVehicles() != null
                ? recall.getRecallElectricVehicles().size() : 0
        );

        return recallsListDTO;
    }

    public void updateRecallRequest(Recall recall, UpdateRecallRequest recallRequest) {
        if(recall == null || recallRequest == null) {
            return;
        }

        if(recall.getName() != null) {
            recall.setName(recallRequest.getName());
        }

        if (recallRequest.getStatus() != null) {
            recall.setStatus(recallRequest.getStatus());
        }

        if (recallRequest.getNotificationSent() != null) {
            recall.setNotificationSent(recallRequest.getNotificationSent());
        }

        if (recallRequest.getIssueDescription() != null) {
            recall.setIssueDescription(recallRequest.getIssueDescription());
        }

        if (recallRequest.getPartsRequired() != null) {
            recall.setPartsRequired(recallRequest.getPartsRequired());
        }

        if (recallRequest.getRequiredAction() != null) {
            recall.setRequiredAction(recallRequest.getRequiredAction());
        }

        if (recallRequest.getEvmApprovalStatus() != null) {
            recall.setEvmApprovalStatus(recallRequest.getEvmApprovalStatus());
        }

        if (recallRequest.getStartDate() != null) {
            recall.setStartDate(recallRequest.getStartDate());
        }

        if(recall.getSpecialty() != null) {
            recall.setSpecialty(recallRequest.getSpecialty());
        }

        // relationships will be updated in service layer
    }

    public ReportInfoDTO toReportDTO(Report report) {
        ReportInfoDTO reportListInfoDTO = new ReportInfoDTO();
        reportListInfoDTO.setReportId(report.getId());
        reportListInfoDTO.setReportName(report.getReportName());
        reportListInfoDTO.setDescription(report.getDescription());
        reportListInfoDTO.setError(report.getError());
        return reportListInfoDTO;
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

    public VehicleBasicInfoDTO toVehicleBasicInfoDTO(ElectricVehicle vehicle) {
        if (vehicle == null) return null;

        VehicleBasicInfoDTO info = new VehicleBasicInfoDTO();
        info.setVehicleId(vehicle.getId());
        info.setVehicleName(vehicle.getName());
        info.setOwner(vehicle.getOwner());
        info.setEmail(vehicle.getEmail());
        info.setPhoneNumber(vehicle.getPhoneNumber());
        info.setPicture(vehicle.getPicture());

        return info;
    }


}

