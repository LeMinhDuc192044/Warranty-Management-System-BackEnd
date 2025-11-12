package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartAvailabilityItemDTO {
    private String partTypeId;
    private String partName;
    private Integer availableQuantity;
    private Boolean isAvailable;
    private String message;
}
