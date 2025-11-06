package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.Recall;
import com.warrantyclaim.warrantyclaim_api.enums.RecallStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecallService {
    public RecallResponseDTO createRecall(RecallCreateDTO createDTO);
    public RecallResponseDTO getRecallById(String id);
    public RecallResponseDTO updateRecall(String id, UpdateRecallRequest updateDTO);
    public void deleteRecall(String id);
    public Page<RecallsListDTO> getAllRecalls(Pageable pageable);
    public RecallResponseDTO updateRecallStatus(String id, RecallStatus statusDTO);
    public RecallResponseDTO updateNotificationSent(String id, Boolean notificationDTO);
    public RecallResponseDTO assignVehicles(String recallId, List<String> request);
    public RecallResponseDTO addVehicle(String recallId, String vehicleId);
    public RecallResponseDTO removeVehicle(String recallId, String vehicleId);
    public RecallResponseDTO assignVehicleTypes(String recallId, List<String> vehicleTypeIds);
    public RecallResponseDTO addVehicleType(String recallId, String vehicleTypeId);
    public RecallResponseDTO removeVehicleType(String recallId, String vehicleTypeId);
    public RecallResponseDTO assignTechnicians(String recallId, List<String> technicianIds);
    public RecallResponseDTO addTechnician(String recallId, String technicianId);
    public RecallResponseDTO removeTechnician(String recallId, String technicianId);
    public ReportInfoListDTO getAllReports(String recallId);
    RecallVehicleStatusResponseDTO updateRecallVehicleStatus(
            String recallId,
            String vehicleId,
            UpdateRecallVehicleStatusDTO statusDTO
    );
    // Thêm vào RecallService interface
    RecallVehicleDetailDTO getRecallVehicleDetail(String recallId, String vehicleId);
    List<Recall> getRecallsByTechnicianId(String technicianId);
}

