package com.warrantyclaim.warrantyclaim_api.entity;

import com.warrantyclaim.warrantyclaim_api.enums.CoverageTypeWarrantyPolicy;
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
@Getter
@Setter
@Table(name = "Warranty_Policy")
public class WarrantyPolicy {

    @Id
    @Column(name = "ID_Warranty_Policy", length = 50)
    private String id;

    @Column(name = "Description", length = 255)
    private String description;

    @Column(name = "Policy_Name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "Coverage_Type")
    private CoverageTypeWarrantyPolicy coverageTypeWarrantyPolicy;

    @Column(name = "Coverage_Duration_Months")
    private Integer coverageDurationMonths;

    @Column(name = "Coverage_Mileage")
    private Float coverageMileage;

    @OneToMany(mappedBy = "warrantyPolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarrantyPolicyElectricVehicleType> warrantyPolicyElectricVehicleTypes = new ArrayList<>();

    @OneToMany(mappedBy = "warrantyPolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarrantyPolicyProductsSparePartsTypeSC> warrantyPolicyProductsSparePartsTypeSCs = new ArrayList<>();

    @OneToMany(mappedBy = "warrantyPolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductsSparePartsTypeEVMWarrantyPolicy> warrantyPoliciesEvmTypes = new ArrayList<>();

    public void addVehicleType(ElectricVehicleType vehicleType) {
        WarrantyPolicyElectricVehicleType junction = new WarrantyPolicyElectricVehicleType();
        junction.setWarrantyPolicyId(this.id);
        junction.setVehicleTypeId(vehicleType.getId());
        junction.setWarrantyPolicy(this);
        junction.setVehicleType(vehicleType);
        warrantyPolicyElectricVehicleTypes.add(junction);
    }

    public void setElectricVehicleTypes(List<ElectricVehicleType> vehicleTypes) {
        this.warrantyPolicyElectricVehicleTypes.clear();
        if (vehicleTypes != null) {
            for (ElectricVehicleType vehicleType : vehicleTypes) {
                addVehicleType(vehicleType); // Use the helper method
            }
        }
    }

    public void removeVehicleType(String vehicleTypeId) {
        warrantyPolicyElectricVehicleTypes.removeIf(wp -> wp.getVehicleTypeId().equals(vehicleTypeId));
    }

    // ============= Helper Methods for SC Spare Parts Types =============

    public void addSparePartsTypeSC(ProductsSparePartsTypeSC sparePartsType) {
        WarrantyPolicyProductsSparePartsTypeSC junction = new WarrantyPolicyProductsSparePartsTypeSC();
        junction.setWarrantyPolicyId(this.id);
        junction.setPartTypeId(sparePartsType.getId());
        junction.setWarrantyPolicy(this);
        junction.setPartType(sparePartsType);
        warrantyPolicyProductsSparePartsTypeSCs.add(junction);
    }

    public void setSparePartsTypesSC(List<ProductsSparePartsTypeSC> sparePartsTypes) {
        this.warrantyPolicyProductsSparePartsTypeSCs.clear();
        if (sparePartsTypes != null) {
            for (ProductsSparePartsTypeSC sparePartsType : sparePartsTypes) {
                addSparePartsTypeSC(sparePartsType); // Use the helper method
            }
        }
    }

    public void removeSparePartsTypeSC(String sparePartsTypeId) {
        warrantyPolicyProductsSparePartsTypeSCs.removeIf(wp -> wp.getPartTypeId().equals(sparePartsTypeId));
    }

    // ============= Helper Methods for EVM Spare Parts Types =============

    public void addSparePartsTypeEVM(ProductsSparePartsTypeEVM sparePartsType) {
        ProductsSparePartsTypeEVMWarrantyPolicy junction = new ProductsSparePartsTypeEVMWarrantyPolicy();
        junction.setWarrantyPolicyId(this.id);
        junction.setPartTypeId(sparePartsType.getId());
        junction.setWarrantyPolicy(this);
        junction.setPartType(sparePartsType);
        warrantyPoliciesEvmTypes.add(junction);
    }

    public void setSparePartsTypesEVM(List<ProductsSparePartsTypeEVM> sparePartsTypes) {
        this.warrantyPoliciesEvmTypes.clear();
        if (sparePartsTypes != null) {
            for (ProductsSparePartsTypeEVM sparePartsType : sparePartsTypes) {
                addSparePartsTypeEVM(sparePartsType); // Use the helper method
            }
        }
    }

    public void removeSparePartsTypeEVM(String sparePartsTypeId) {
        warrantyPoliciesEvmTypes.removeIf(wp -> wp.getPartTypeId().equals(sparePartsTypeId));
    }

}
