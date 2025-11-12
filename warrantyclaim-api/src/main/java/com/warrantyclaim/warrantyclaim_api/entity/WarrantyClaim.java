package com.warrantyclaim.warrantyclaim_api.entity;

import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Warranty_Claim")
public class WarrantyClaim {
    @Id
    @Column(name = "ClaimID", length = 50)
    private String id;

    @Column(length = 100)
    private String customerName;

    @Column(length = 20)
    private String customerPhone;

    private LocalDate claimDate;

    @Column(name = "RequiredParts")
    private String requiredParts;

    @Column(length = 100)
    private String issueDescription;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private WarrantyClaimStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "Office_Branch")
    private OfficeBranch officeBranch;

    @Column(length = 500)
    private String rejectionReason;

    @Column(name = "CreatedByUserId")
    private Long createdByUserId;

    @Column(length = 100)
    private String email;

    @Column(name = "Return_Date")
    private LocalDate returnDate;

    @Column(name = "Work_Start_Time")
    private LocalDateTime workStartTime;

    @Column(name = "Work_End_Time")
    private LocalDateTime workEndTime;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Vehicle_VIN_ID")
    private ElectricVehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "SC_StaffID")
    private SCStaff staff;

    @ManyToOne
    @JoinColumn(name = "SC_TechnicianID")
    private SCTechnician technician;

}
