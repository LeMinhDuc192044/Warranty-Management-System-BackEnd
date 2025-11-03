package com.warrantyclaim.warrantyclaim_api.mapper;

import com.warrantyclaim.warrantyclaim_api.dto.ReportDTO;
import com.warrantyclaim.warrantyclaim_api.entity.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    
    public ReportDTO toDTO(Report report) {
        if (report == null) {
            return null;
        }
        
        ReportDTO dto = new ReportDTO();
        dto.setId(report.getId());
        dto.setTitle(report.getReportName());
        dto.setDescription(report.getDescription());
        dto.setStatus(report.getStatus());
        dto.setError(report.getError());
        
        if (report.getCampaign() != null) {
            dto.setServiceCampaignId(report.getCampaign().getId());
        }
        if (report.getRecall() != null) {
            dto.setRecallId(report.getRecall().getId());
        }
        if (report.getCampaign() != null) {
            dto.setWarrantyClaimId(report.getCampaign().getId());
        }
        
        return dto;
    }
}