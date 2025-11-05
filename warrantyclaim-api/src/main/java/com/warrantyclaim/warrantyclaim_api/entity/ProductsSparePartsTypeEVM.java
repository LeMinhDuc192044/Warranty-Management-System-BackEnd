package com.warrantyclaim.warrantyclaim_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "Products_Spare_Parts_Type_EVM")
public class ProductsSparePartsTypeEVM {

    @Id
    @Column(name = "ID_Products_Part_Type_EVM", length = 50)
    private String id;

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

    @Column(name = "`Condition`", length = 50)
    private String condition;

    @Column(name = "Stock_Status", length = 20)
    private String stockStatus; // Values: IN_STOCK, LOW_STOCK, OUT_OF_STOCK

    @OneToMany(mappedBy = "partType")
    private List<ProductsSparePartsEVM> productsSparePartsEVMS = new ArrayList<>();

}
