package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartsEvmTransferMultipleResponse {

    private String id;
    private String name;
    private VehicleType vehicleType;
    private PartStatus oldCondition;
    private PartStatus newCondition;
    private OfficeBranch targetOfficeBranch;
    private LocalDateTime transferredAt;
    private Boolean success;
    private String errorMessage;

    // For summary
    private String newScId;
}
