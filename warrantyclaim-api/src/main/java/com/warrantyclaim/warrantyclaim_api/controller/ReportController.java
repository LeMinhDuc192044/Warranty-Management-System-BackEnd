package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.CreateReportRequest;
import com.warrantyclaim.warrantyclaim_api.dto.ReportDTO;
import com.warrantyclaim.warrantyclaim_api.dto.UpdateReportRequest;
import com.warrantyclaim.warrantyclaim_api.enums.ReportStatus;
import com.warrantyclaim.warrantyclaim_api.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Report Management", description = "APIs for managing reports")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "Create a new report")
    public ResponseEntity<ReportDTO> createReport(@Valid @RequestBody CreateReportRequest request) {
        return new ResponseEntity<>(reportService.createReport(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a report by ID")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable String id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReportDTO> updateStatus(@PathVariable String id, @RequestParam ReportStatus status) {
        return ResponseEntity.ok(reportService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<ReportDTO> updateImage(@PathVariable String id, @RequestParam String image) {
        return ResponseEntity.ok(reportService.updateImage(id, image));
    }

    @GetMapping
    @Operation(summary = "Get all reports")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a report")
    public ResponseEntity<ReportDTO> updateReport(
            @PathVariable String id,
            @Valid @RequestBody UpdateReportRequest request) {
        return ResponseEntity.ok(reportService.updateReport(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a report")
    public ResponseEntity<Void> deleteReport(@PathVariable String id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reportId}/service-campaigns/{serviceCampaignId}")
    @Operation(summary = "Assign a service campaign to a report")
    public ResponseEntity<ReportDTO> assignServiceCampaign(
            @PathVariable String reportId,
            @PathVariable String serviceCampaignId) {
        return ResponseEntity.ok(reportService.assignServiceCampaign(reportId, serviceCampaignId));
    }

    @PostMapping("/{reportId}/recalls/{recallId}")
    @Operation(summary = "Assign a recall to a report")
    public ResponseEntity<ReportDTO> assignRecall(
            @PathVariable String reportId,
            @PathVariable String recallId) {
        return ResponseEntity.ok(reportService.assignRecall(reportId, recallId));
    }

    @PostMapping("/{reportId}/warranty-claims/{warrantyClaimId}")
    @Operation(summary = "Assign a warranty claim to a report")
    public ResponseEntity<ReportDTO> assignWarrantyClaim(
            @PathVariable String reportId,
            @PathVariable String warrantyClaimId) {
        return ResponseEntity.ok(reportService.assignWarrantyClaim(reportId, warrantyClaimId));
    }
}