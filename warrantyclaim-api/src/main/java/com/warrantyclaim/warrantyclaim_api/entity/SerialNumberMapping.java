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
@Table(name = "Serial_Number_Mapping")
public class SerialNumberMapping {

    @Id
    @Column(name = "Serial_Number", length = 100)
    private String serialNumber;

    @Column(name = "Part_ID", length = 50, nullable = false)
    private String partId;

    @Column(name = "Vehicle_VIN", length = 50, nullable = false)
    private String vehicleVIN;

    @Column(name = "Claim_ID", length = 50)
    private String claimId;

    @Column(name = "Mapping_Date", nullable = false)
    private LocalDateTime mappingDate;

    @Column(name = "Mapped_By_Technician_ID", length = 50)
    private String mappedByTechnicianId;

    @Column(name = "Notes", length = 500)
    private String notes;

    @Column(name = "Durability_Percentage")
    private Integer durabilityPercentage;

    @ManyToOne
    @JoinColumn(name = "Part_ID", insertable = false, updatable = false)
    private ProductsSparePartsSC sparePart;

    @ManyToOne
    @JoinColumn(name = "Vehicle_VIN", insertable = false, updatable = false)
    private ElectricVehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "Claim_ID", insertable = false, updatable = false)
    private WarrantyClaim warrantyClaim;
}
