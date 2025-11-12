package com.warrantyclaim.warrantyclaim_api.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "Warranty_Policy_Electric_Vehicle_Type")
@IdClass(WarrantyPolicyElectricVehicleTypeId.class)
public class WarrantyPolicyElectricVehicleType {

    @Id
    @Column(name = "ID_Warranty_Policy", length = 50)
    private String warrantyPolicyId;

    @Id
    @Column(name = "ID_Electric_Vehicle_Type", length = 50)
    private String vehicleTypeId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_Warranty_Policy", insertable = false, updatable = false)
    private WarrantyPolicy warrantyPolicy;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_Electric_Vehicle_Type", insertable = false, updatable = false)
    private ElectricVehicleType vehicleType;
}
