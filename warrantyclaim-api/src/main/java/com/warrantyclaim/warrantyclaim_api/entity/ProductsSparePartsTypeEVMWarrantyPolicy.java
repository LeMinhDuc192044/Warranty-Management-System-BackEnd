package com.warrantyclaim.warrantyclaim_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "Products_SparePartsTypeEVM_WarrantyPolicy")
@IdClass(ProductsSparePartsTypeEVMWarrantyPolicyId.class)
public class ProductsSparePartsTypeEVMWarrantyPolicy {

    @Id
    @Column(name = "ID_Products_Part_Type_EVM", length = 50)
    private String partTypeId;

    @Id
    @Column(name = "ID_Warranty_Policy", length = 50)
    private String warrantyPolicyId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_Products_Part_Type_EVM", insertable = false, updatable = false)
    private ProductsSparePartsTypeEVM partType;

<<<<<<< HEAD
=======

>>>>>>> origin/main
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_Warranty_Policy", insertable = false, updatable = false)
    private WarrantyPolicy warrantyPolicy;
}
