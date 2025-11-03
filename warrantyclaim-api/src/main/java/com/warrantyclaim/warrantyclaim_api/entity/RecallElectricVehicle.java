package com.warrantyclaim.warrantyclaim_api.entity;
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

@Table(name = "Recall_Electric_Vehicle")
@IdClass(RecallElectricVehicleId.class)
public class RecallElectricVehicle {

    @Id
    @Column(name = "Recall_ID", length = 50)
    private String recallId;

    @Id
    @Column(name = "Vehicle_VIN_ID", length = 50)
    private String vehicleId;

    @Column
    private String status;

    @ManyToOne
    @JoinColumn(name = "Recall_ID", insertable = false, updatable = false)
    private Recall recall;

    @ManyToOne
    @JoinColumn(name = "Vehicle_VIN_ID", insertable = false, updatable = false)
    private ElectricVehicle vehicle;
}
