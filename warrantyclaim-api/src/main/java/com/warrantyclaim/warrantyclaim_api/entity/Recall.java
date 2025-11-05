package com.warrantyclaim.warrantyclaim_api.entity;

import com.warrantyclaim.warrantyclaim_api.enums.EvmApprovalStatus;
import com.warrantyclaim.warrantyclaim_api.enums.RecallStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Recall")
public class Recall {
    @Id
    @Column(name = "Recall_ID", length = 50)
    private String id;

    @Column(name = "RecallName", length = 100)
    private String name;

    @Column(length = 255)
    private String issueDescription;

    private LocalDate startDate;

    @Column(length = 255)
    private String requiredAction;

    @Column(length = 255)
    private String partsRequired;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private RecallStatus status;

    private Boolean notificationSent;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private EvmApprovalStatus evmApprovalStatus;

    @Column(name = "Target_District", length = 500)
    private String targetDistrict; // Comma-separated districts: "Quận 1,Quận 2,Quận 3"

    @OneToMany(mappedBy = "recall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElectricVehicleTypeRecall> vehicleTypeRecalls = new ArrayList<>();

    @OneToMany(mappedBy = "recall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecallElectricVehicle> recallElectricVehicles = new ArrayList<>();

    @OneToMany(mappedBy = "recall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecallSCTechnician> techniciansRecall = new ArrayList<>();

    @OneToMany(mappedBy = "recall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public void addVehicleType(ElectricVehicleType vehicleType) {
        ElectricVehicleTypeRecall junction = new ElectricVehicleTypeRecall();
        junction.setVehicleTypeId(vehicleType.getId());
        junction.setRecallId(this.id);
        junction.setVehicleType(vehicleType);
        junction.setRecall(this);
        vehicleTypeRecalls.add(junction);
    }

    public void setElectricVehicleTypes(List<ElectricVehicleType> vehicleTypes) {
        // Clear old relationships first (important for orphanRemoval = true)
        this.vehicleTypeRecalls.clear();

        if (vehicleTypes != null) {
            for (ElectricVehicleType vehicleType : vehicleTypes) {
                ElectricVehicleTypeRecall junction = new ElectricVehicleTypeRecall();
                junction.setVehicleTypeId(vehicleType.getId()); // adjust getter name if different
                junction.setRecallId(this.id);
                junction.setVehicleType(vehicleType);
                junction.setRecall(this);
                this.vehicleTypeRecalls.add(junction);
            }
        }
    }

    public void removeVehicleType(String vehicleTypeId) {
        vehicleTypeRecalls.removeIf(vtc -> vtc.getVehicleTypeId().equals(vehicleTypeId));
    }

    public void addTechnician(SCTechnician technician) {
        RecallSCTechnician junction = new RecallSCTechnician();
        junction.setTechnicianId(technician.getId());
        junction.setRecallId(this.id);
        junction.setTechnician(technician);
        junction.setRecall(this);
        techniciansRecall.add(junction);
    }

    public void setScTechnicians(List<SCTechnician> technicians) {
        // Clear old technician links first
        this.techniciansRecall.clear();

        if (technicians != null) {
            for (SCTechnician technician : technicians) {
                RecallSCTechnician junction = new RecallSCTechnician();
                junction.setTechnicianId(technician.getId()); // adjust getter if needed
                junction.setRecallId(this.id);
                junction.setTechnician(technician);
                junction.setRecall(this);
                this.techniciansRecall.add(junction);
            }
        }
    }

    public void removeTechnician(String technicianId) {
        techniciansRecall.removeIf(tc -> tc.getTechnicianId().equals(technicianId));
    }

    public void addElectricVehicle(ElectricVehicle vehicle) {
        RecallElectricVehicle junction = new RecallElectricVehicle();
        junction.setVehicleId(vehicle.getId());
        junction.setRecallId(this.id);
        junction.setVehicle(vehicle);
        junction.setRecall(this);
        recallElectricVehicles.add(junction);
    }

    public void setElectricVehicles(List<ElectricVehicle> vehicles) {
        // Clear old technician links first
        this.recallElectricVehicles.clear();

        if (vehicles != null) {
            for (ElectricVehicle vehicle : vehicles) {
                RecallElectricVehicle junction = new RecallElectricVehicle();
                junction.setVehicleId(vehicle.getId()); // adjust getter if needed
                junction.setRecallId(this.id);
                junction.setVehicle(vehicle);
                junction.setRecall(this);
                this.recallElectricVehicles.add(junction);
            }
        }
    }

    public void removeElectricVehicle(String vehicleId) {
        recallElectricVehicles.removeIf(tc -> tc.getVehicleId().equals(vehicleId));
    }

}
