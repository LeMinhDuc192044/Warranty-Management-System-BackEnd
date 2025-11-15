package com.warrantyclaim.warrantyclaim_api.service.Implement;

import com.warrantyclaim.warrantyclaim_api.dto.CreateReportRequest;
import com.warrantyclaim.warrantyclaim_api.dto.ReportDTO;
import com.warrantyclaim.warrantyclaim_api.dto.UpdateReportRequest;
import com.warrantyclaim.warrantyclaim_api.entity.Report;
import com.warrantyclaim.warrantyclaim_api.entity.Recall;
import com.warrantyclaim.warrantyclaim_api.entity.ServiceCampaigns;
import com.warrantyclaim.warrantyclaim_api.entity.WarrantyClaim;
import com.warrantyclaim.warrantyclaim_api.enums.ReportStatus;
import com.warrantyclaim.warrantyclaim_api.exception.ResourceNotFoundException;
import com.warrantyclaim.warrantyclaim_api.mapper.ReportMapper;
import com.warrantyclaim.warrantyclaim_api.repository.ReportRepository;
import com.warrantyclaim.warrantyclaim_api.repository.RecallRepository;
import com.warrantyclaim.warrantyclaim_api.repository.ServiceCampaignsRepository;
import com.warrantyclaim.warrantyclaim_api.repository.WarrantyClaimRepository;
import com.warrantyclaim.warrantyclaim_api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ServiceCampaignsRepository serviceCampaignRepository;
    private final RecallRepository recallRepository;
    private final WarrantyClaimRepository warrantyClaimRepository;
    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public ReportDTO createReport(CreateReportRequest request) {
        Report report = new Report();
        report.setId(generatedIdRecall());
        report.setReportName(request.getTitle());
        report.setDescription(request.getDescription());
        report.setError(request.getError());
//        report.setAmountOfCar(request.getAmountOfCar());
        report.setStatus(ReportStatus.PENDING);
        return reportMapper.toDTO(reportRepository.save(report));
    }

    public String generatedIdRecall() {
        return "REP-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public ReportDTO getReportById(String id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        return reportMapper.toDTO(report);
    }

    @Override
    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReportDTO updateReport(String id, UpdateReportRequest request) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        
        if (request.getTitle() != null) {
            report.setReportName(request.getTitle());
        }
        if (request.getDescription() != null) {
            report.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            report.setStatus(request.getStatus());
        }

        if(request.getError() != null) {
            report.setError(request.getError());
        }

        if(request.getImage() != null) {
            report.setImage(request.getImage());
        }

//        if(request.getAmountOfCar() != null) {
//            report.setAmountOfCar(request.getAmountOfCar());
//        }

        return reportMapper.toDTO(reportRepository.save(report));
    }

    @Override
    @Transactional
    public void deleteReport(String id) {
        if (!reportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }


    @Override
    @Transactional
    public ReportDTO assignServiceCampaign(String reportId, String serviceCampaignId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        
        ServiceCampaigns serviceCampaign = serviceCampaignRepository.findById(serviceCampaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Service Campaign not found with id: " + serviceCampaignId));
        
        report.setCampaign(serviceCampaign);
        return reportMapper.toDTO(reportRepository.save(report));
    }

    @Override
    @Transactional
    public ReportDTO assignRecall(String reportId, String recallId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        
        Recall recall = recallRepository.findById(recallId)
                .orElseThrow(() -> new ResourceNotFoundException("Recall not found with id: " + recallId));
        
        report.setRecall(recall);
        return reportMapper.toDTO(reportRepository.save(report));
    }

    @Override
    @Transactional
    public ReportDTO assignWarrantyClaim(String reportId, String warrantyClaimId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));
        
        WarrantyClaim warrantyClaim = warrantyClaimRepository.findById(warrantyClaimId)
                .orElseThrow(() -> new ResourceNotFoundException("Warranty Claim not found with id: " + warrantyClaimId));
        
        report.setWarrantyClaim(warrantyClaim);
        return reportMapper.toDTO(reportRepository.save(report));
    }

    @Override
    @Transactional
    public ReportDTO updateStatus(String reportId, ReportStatus status) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

        report.setStatus(status);
        reportRepository.save(report);
        return reportMapper.toDTO(report);
    }

    @Override
    @Transactional
    public ReportDTO updateImage(String reportId, String image) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

        report.setImage(image);
        reportRepository.save(report);
        return reportMapper.toDTO(report);
    }
}