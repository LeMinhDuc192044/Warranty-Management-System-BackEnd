package com.warrantyclaim.warrantyclaim_api.entity;

import com.warrantyclaim.warrantyclaim_api.enums.PartsRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

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
