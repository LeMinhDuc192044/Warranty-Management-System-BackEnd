package com.warrantyclaim.warrantyclaim_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Table(name = "Products_Spare_Parts_Type_SC")
public class ProductsSparePartsTypeSC {
    @Id
    @Column(name = "ID_Products_Part_Type_SC", length = 50)
    private String id;

    @Column(name = "Part_Type_ID_EVM", length = 50)
    private String partTypeIdEVM; // Reference to EVM part type

    @Column(name = "Branch_Office", length = 100, nullable = false)
    private String branchOffice; // SC branch location (district)

    @Column(length = 45)
    private String description;

    @Column(name = "Part_Name", length = 100)
    private String partName;

    @Column(name = "Year_Model_Year")
    private Integer yearModelYear;

    @Column(name = "Total_Amount_Of_Product")
    private Integer totalAmountOfProduct;

    private Float price;

    @Column(length = 100)
    private String manufacturer;

    @Column(name = "Condition", length = 50)
    private String condition;

    @Column(name = "Stock_Status", length = 20)
    private String stockStatus; // Values: IN_STOCK, LOW_STOCK, OUT_OF_STOCK

    @Column(name = "Last_Updated")
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "partType")
    private List<ProductsSparePartsSC> productsSparePartsSC = new ArrayList<>();

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
        updateStockStatus();
    }

    public void updateStockStatus() {
        if (this.totalAmountOfProduct == null || this.totalAmountOfProduct == 0) {
            this.stockStatus = "OUT_OF_STOCK";
        } else if (this.totalAmountOfProduct <= 10) {
            this.stockStatus = "LOW_STOCK";
        } else {
            this.stockStatus = "IN_STOCK";
        }
    }
}
