package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.CoverageTypeWarrantyPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarrantyPolicyListDTO {
    private String id;
    private String policyName;
    private String description;
    private Integer coverageDurationMonths;
    private CoverageTypeWarrantyPolicy coverageType;
    private Integer vehicleTypeCount;
    private Integer sparePartsTypeSCCount;
    private Integer sparePartsTypeEVMCount;
}
