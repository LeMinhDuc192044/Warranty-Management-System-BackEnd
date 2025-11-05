package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.enums.ServiceCampaignsStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ServiceCampaignsService {
    public ServiceCampaignsResponseDTO createServiceCampaigns(ServiceCampaignsRequestDTO requestDTO);

    public ServiceCampaignsResponseDTO getCampaignById(String id);
    public ServiceCampaignsResponseDTO updateCampaign(String id, ServiceCampaignsUpdateDTO request);
    public Page<ServiceCampaignsListDTO> getAllCampaigns(Pageable pageable);
    public void deleteCampaign(String id);
    public ServiceCampaignsResponseDTO assignVehicleTypes(String campaignId, AssignVehicleTypesDTO request);
    public ServiceCampaignsResponseDTO addVehicleType(String campaignId, String vehicleTypeId);
    public ServiceCampaignsResponseDTO removeVehicleType(String campaignId, String vehicleTypeId);
    public ServiceCampaignsResponseDTO assignTechnicians(String campaignId, AssignTechniciansDTO request);
    public ServiceCampaignsResponseDTO addTechnician(String campaignId, String technicianId);
    public ServiceCampaignsResponseDTO removeTechnician(String campaignId, String technicianId);
    public ServiceCampaignsResponseDTO updateDate(String campaignId, LocalDate startDate, LocalDate endDate);
    public ReportInfoListDTO getAllReport(String campaignId);
    public ServiceCampaignsResponseDTO updateServiceCampaignStatus(String id, ServiceCampaignsStatus statusDTO);
    public ServiceCampaignsResponseDTO updateNotificationSent(String id, Boolean notificationDTO);

}
