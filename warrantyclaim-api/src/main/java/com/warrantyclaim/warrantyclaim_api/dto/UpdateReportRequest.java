package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.ReportStatus;
import lombok.Data;

@Data
public class UpdateReportRequest {
    private String title;
    private String description;
    private ReportStatus status;
    private String error;
    private String image;
}