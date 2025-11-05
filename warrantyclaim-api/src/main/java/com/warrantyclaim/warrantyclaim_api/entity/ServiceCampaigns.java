package com.warrantyclaim.warrantyclaim_api.entity;

import com.warrantyclaim.warrantyclaim_api.enums.ServiceCampaignsStatus;
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
@Table(name = "Service_Campaigns")
public class ServiceCampaigns {
    @Id
    @Column(name = "CampaignsID", length = 50)
    private String id;

    @Column(name = "CampaignsTypeName", length = 100)
    private String typeName;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "RequiredParts", length = 200)
    private String requiredParts;

    @Column(name = "Description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 50)
    private ServiceCampaignsStatus status;

    @Column(name = "NotificationSent")
    private Boolean notificationSent;

    @Column(name = "Target_District", length = 500)
    private String targetDistrict; // Comma-separated districts: "Quận 1,Quận 2,Quận 3"

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ElectricVehicleTypeServiceCampaigns> vehicleTypeCampaigns = new ArrayList<>();

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceCampaignsSCTechnician> technicianCampaigns = new ArrayList<>();

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    public void addVehicleType(ElectricVehicleType vehicleType) {
        ElectricVehicleTypeServiceCampaigns junction = new ElectricVehicleTypeServiceCampaigns();
        junction.setVehicleTypeId(vehicleType.getId());
        junction.setCampaignId(this.id);
        junction.setVehicleType(vehicleType);
        junction.setCampaign(this);
        vehicleTypeCampaigns.add(junction);
    }

    public void setElectricVehicleTypes(List<ElectricVehicleType> vehicleTypes) {
        // Clear old relationships first (important for orphanRemoval = true)
        this.vehicleTypeCampaigns.clear();

        if (vehicleTypes != null) {
            for (ElectricVehicleType vehicleType : vehicleTypes) {
                ElectricVehicleTypeServiceCampaigns junction = new ElectricVehicleTypeServiceCampaigns();
                junction.setVehicleTypeId(vehicleType.getId()); // adjust getter name if different
                junction.setCampaignId(this.id);
                junction.setVehicleType(vehicleType);
                junction.setCampaign(this);
                this.vehicleTypeCampaigns.add(junction);
            }
        }
    }

    public void removeVehicleType(String vehicleTypeId) {
        vehicleTypeCampaigns.removeIf(vtc -> vtc.getVehicleTypeId().equals(vehicleTypeId));
    }

    public void addTechnician(SCTechnician technician) {
        ServiceCampaignsSCTechnician junction = new ServiceCampaignsSCTechnician();
        junction.setTechnicianId(technician.getId());
        junction.setCampaignId(this.id);
        junction.setTechnician(technician);
        junction.setCampaign(this);
        technicianCampaigns.add(junction);
    }

    public void setScTechnicians(List<SCTechnician> technicians) {
        // Clear old technician links first
        this.technicianCampaigns.clear();

        if (technicians != null) {
            for (SCTechnician technician : technicians) {
                ServiceCampaignsSCTechnician junction = new ServiceCampaignsSCTechnician();
                junction.setTechnicianId(technician.getId()); // adjust getter if needed
                junction.setCampaignId(this.id);
                junction.setTechnician(technician);
                junction.setCampaign(this);
                this.technicianCampaigns.add(junction);
            }
        }
    }

    public void removeTechnician(String technicianId) {
        technicianCampaigns.removeIf(tc -> tc.getTechnicianId().equals(technicianId));
    }
}
