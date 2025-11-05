package com.warrantyclaim.warrantyclaim_api.entity;

import com.warrantyclaim.warrantyclaim_api.enums.PartsRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Parts_Request")
public class PartsRequest {
    @Id
    @Column(name = "RequestID", length = 50)
    private String id;
//
//    @Column(name = "PartNumber", length = 50)
//    private String partNumber;

    @Column(name = "PartName", length = 100)
    private String partName;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "RequestDate")
    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private PartsRequestStatus status;

    @Column(name = "DeliveryDate")
    private LocalDate deliveryDate;

    @Column(name = "RequestedByStaffID", length = 50)
    private String requestedByStaffId;

    @Column(name = "BranchOffice", length = 100)
    private String branchOffice;

    // ===== NEW TRACKING FIELDS =====
    @Column(name = "Delivery_Status", length = 50)
    private String deliveryStatus; // PENDING, APPROVED, SHIPPING, DELIVERED, REJECTED

    @Column(name = "EVM_Stock_Before")
    private Integer evmStockBefore;

    @Column(name = "EVM_Stock_After")
    private Integer evmStockAfter;

    @Column(name = "SC_Stock_Before")
    private Integer scStockBefore;

    @Column(name = "SC_Stock_After")
    private Integer scStockAfter;

    @Column(name = "Approved_By_EVM_Staff_ID", length = 50)
    private String approvedByEvmStaffId;

    @Column(name = "Approved_At")
    private LocalDateTime approvedAt;

    @Column(name = "Rejection_Reason", length = 500)
    private String rejectionReason;

    @Column(name = "Received_By_SC_Admin_ID", length = 50)
    private String receivedByScAdminId;

    @Column(name = "Received_At")
    private LocalDateTime receivedAt;
    // ===== END NEW FIELDS =====

    // Link to EVM Part Type (Central warehouse/Master data)
    @ManyToOne
    @JoinColumn(name = "ID_Products_Part_Type_EVM")
    private ProductsSparePartsTypeEVM partType;

    @ManyToOne
    @JoinColumn(name = "Vehicle_VIN_ID")
    private ElectricVehicle electricVehicle;

    // SC Staff who created the request
    @ManyToOne
    @JoinColumn(name = "SC_StaffID")
    private SCStaff requestedBy;

    // EVM Staff who approved/rejected the request
    @ManyToOne
    @JoinColumn(name = "EVM_StaffID")
    private EVMStaff approvedBy;
}
