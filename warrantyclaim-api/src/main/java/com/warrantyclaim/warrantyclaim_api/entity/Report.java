package com.warrantyclaim.warrantyclaim_api.entity;


import com.warrantyclaim.warrantyclaim_api.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



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
}