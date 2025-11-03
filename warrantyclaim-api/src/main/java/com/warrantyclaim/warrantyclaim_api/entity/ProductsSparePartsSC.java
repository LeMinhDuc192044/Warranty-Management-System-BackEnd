package com.warrantyclaim.warrantyclaim_api.entity;

import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleType;
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
@Table(name = "Products_Spare_Parts_SC")
public class ProductsSparePartsSC {
    @Id
    @Column(name = "ID_Product_Serial_SC", length = 50)
    private String id;

    @Column(name = "Name_Product", length = 100)
    private String name;

    @Column(name = "'Condition'", length = 50)
    private PartStatus condition;

    @Enumerated(EnumType.STRING)
    @Column(name = "Vehicle_Type")
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "Office_Branch")
    private OfficeBranch officeBranch;

    @ManyToOne
    @JoinColumn(name = "ID_Products_Part_Type_SC")
    private ProductsSparePartsTypeSC partType;


    @ManyToOne
    @JoinColumn(name = "Vehicle_VIN_ID")
    private ElectricVehicle electricVehicle;
}
