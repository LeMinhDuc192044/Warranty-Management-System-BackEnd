package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.CoverageTypeWarrantyPolicy;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantyPolicyCreateDTO {

    @NotBlank(message = "Policy name must not be blank!!!")
    private String policyName;

    @NotBlank(message = "Description must not be blank!!!")
    private String description;

    private Integer coverageDurationMonths;

    private CoverageTypeWarrantyPolicy coverageType;

    private List<String> vehicleTypeIds;
    private List<String> sparePartsTypeSCIds;
    private List<String> sparePartsTypeEVMIds;
}