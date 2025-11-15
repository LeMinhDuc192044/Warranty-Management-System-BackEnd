package com.warrantyclaim.warrantyclaim_api.entity;

import com.warrantyclaim.warrantyclaim_api.enums.UsageType;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
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
@Table(name = "Electric_Vehicle")
public class ElectricVehicle {
    @Id
    @Column(name = "Vehicle_VIN_ID", length = 50)
    private String id;

    @Column(name = "Vehicle_Name", length = 100)
    private String name;

    @Column(name = "Total_KM")
    private Float totalKm;

    @Column(length = 250)
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(name = "Usage_Type", length = 20)
    private UsageType usageType;

    @Column(name = "Purchase_Date")
    private LocalDate purchaseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Usage_Type", length = 20, nullable = false)
    private com.warrantyclaim.warrantyclaim_api.enums.UsageType usageType;

    @Column(length = 100)
    private String owner;

    @Column(name = "Phone_Number", length = 20)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Column(name = "Return_Date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private VehicleStatus status;

    @Column(name = "Version", length = 50)
    private String version;

    @ManyToOne
    @JoinColumn(name = "ID_Electric_Vehicle_Type")
    private ElectricVehicleType vehicleType;

    @OneToMany(mappedBy = "electricVehicle")
    private List<ProductsSparePartsSC> productsSparePartsSC = new ArrayList<>();
}
