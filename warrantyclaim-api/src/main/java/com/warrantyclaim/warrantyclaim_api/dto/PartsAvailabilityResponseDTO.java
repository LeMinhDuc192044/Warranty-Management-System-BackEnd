package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartsAvailabilityResponseDTO {
    private String claimId;
    private String officeBranch;
    private List<PartAvailabilityItemDTO> parts;
    private Boolean allPartsAvailable;
    private String overallMessage;
}
