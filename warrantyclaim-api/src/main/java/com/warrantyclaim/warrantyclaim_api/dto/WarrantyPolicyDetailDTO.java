package com.warrantyclaim.warrantyclaim_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyPolicyDetailDTO {

    private String policyId;

    private String policyName;

    private String description;

    private String coverageType; // FULL, PARTIAL, BATTERY_ONLY, etc.

    private Integer coverageDurationMonths;

    private Float coverageMileage; // Coverage mileage in KM

    @JsonProperty("isApplicable")
    private boolean isApplicable; // Policy này có áp dụng được cho xe này không

    private java.util.List<String> reasons; // Lý do không áp dụng (nếu có)
}
