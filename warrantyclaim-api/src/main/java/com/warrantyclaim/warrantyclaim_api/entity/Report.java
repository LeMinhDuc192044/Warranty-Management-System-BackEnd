package com.warrantyclaim.warrantyclaim_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Report")
public class Report {
    @Id
    @Column(name = "ID_Report", length = 50)
    private String id;

    @Column(length = 45)
    private String description;

    @Column(length = 45)
    private String error;

    @Column(length = 255)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private ReportStatus status;

    @Column(name = "ReportName", length = 100)
    private String reportName;

    // ===== NEW COMPREHENSIVE REPORT FIELDS =====
    @Column(name = "Vehicle_VIN_ID", length = 50)
    private String vehicleVinId;

    @Column(name = "Issue_Description", columnDefinition = "TEXT")
    private String issueDescription;

    @Column(name = "Parts_Used", columnDefinition = "TEXT")
    private String partsUsed; // JSON or comma-separated

    @Column(name = "Serial_Numbers", columnDefinition = "TEXT")
    private String serialNumbers; // JSON or comma-separated

    @Column(name = "Total_Cost")
    private Double totalCost;

    @Column(name = "Submitted_By_SC_Staff_ID", length = 50)
    private String submittedByScStaffId;

    @Column(name = "Submitted_At")
    private LocalDateTime submittedAt;

    @Column(name = "Reviewed_By_SC_Admin_ID", length = 50)
    private String reviewedByScAdminId;

    @Column(name = "Reviewed_At")
    private LocalDateTime reviewedAt;

    @Column(name = "Forwarded_To_EVM_Admin")
    private Boolean forwardedToEvmAdmin;

    @Column(name = "Forwarded_At")
    private LocalDateTime forwardedAt;

    @Column(name = "Report_Type", length = 50)
    private String reportType; // WARRANTY_CLAIM, SERVICE_CAMPAIGN, RECALL, GENERAL
    // ===== END NEW FIELDS =====

    @ManyToOne
    @JoinColumn(name = "CampaignsID")
    private ServiceCampaigns campaign;

    @OneToOne
    @JoinColumn(name = "ClaimID")
    private WarrantyClaim warrantyClaim;

    @ManyToOne
    @JoinColumn(name = "Recall_ID")
    private Recall recall;

    @ManyToOne
    @JoinColumn(name = "SC_StaffID")
    private SCStaff staff;

    @ManyToOne
    @JoinColumn(name = "EVM_Staff_ID")
    private EVMStaff evm;

    @ManyToOne
    @JoinColumn(name = "Vehicle_VIN_ID", insertable = false, updatable = false)
    private ElectricVehicle vehicle;
}