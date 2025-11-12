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
@Setter
@Getter
@Table(name = "Warranty_Policy_Products_Spare_Parts_Type_SC")
@IdClass(WarrantyPolicyProductsSparePartsTypeSCId.class)
public class WarrantyPolicyProductsSparePartsTypeSC {

    @Id
    @Column(name = "ID_Warranty_Policy", length = 50)
    private String warrantyPolicyId;

    @Id
    @Column(name = "ID_Products_Part_Type_SC", length = 50)
    private String partTypeId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_Warranty_Policy", insertable = false, updatable = false)
    private WarrantyPolicy warrantyPolicy;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ID_Products_Part_Type_SC", insertable = false, updatable = false)
    private ProductsSparePartsTypeSC partType;
}
