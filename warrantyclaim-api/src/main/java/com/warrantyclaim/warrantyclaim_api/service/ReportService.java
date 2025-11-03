package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.CreateReportRequest;
import com.warrantyclaim.warrantyclaim_api.dto.ReportDTO;
import com.warrantyclaim.warrantyclaim_api.dto.UpdateReportRequest;
import com.warrantyclaim.warrantyclaim_api.enums.ReportStatus;

import java.util.List;

public interface ReportService {
    ReportDTO createReport(CreateReportRequest request);
    ReportDTO getReportById(String id);
    List<ReportDTO> getAllReports();
    ReportDTO updateReport(String id, UpdateReportRequest request);
    void deleteReport(String id);
    ReportDTO assignServiceCampaign(String reportId, String serviceCampaignId);
    ReportDTO assignRecall(String reportId, String recallId);
    ReportDTO assignWarrantyClaim(String reportId, String warrantyClaimId);
    public ReportDTO updateStatus(String reportId, ReportStatus status);
}