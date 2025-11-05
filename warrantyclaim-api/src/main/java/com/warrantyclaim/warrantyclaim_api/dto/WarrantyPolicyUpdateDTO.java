package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.CoverageTypeWarrantyPolicy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyPolicyUpdateDTO {
    private String policyName;
    private String description;
    private Integer coverageDurationMonths;
    private CoverageTypeWarrantyPolicy coverageType;

    private List<String> vehicleTypeIds;
    private List<String> sparePartsTypeSCIds;
    private List<String> sparePartsTypeEVMIds;
}