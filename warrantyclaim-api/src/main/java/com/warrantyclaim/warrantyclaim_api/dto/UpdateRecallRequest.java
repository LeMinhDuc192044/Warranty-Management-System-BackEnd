package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.EvmApprovalStatus;
import com.warrantyclaim.warrantyclaim_api.enums.RecallStatus;
import com.warrantyclaim.warrantyclaim_api.enums.Specialty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRecallRequest {
    private String name;
    private String issueDescription;
    private LocalDate startDate;
    private String requiredAction;
    private String partsRequired;
    private RecallStatus status;
    private Boolean notificationSent;
    private EvmApprovalStatus evmApprovalStatus;
    private Specialty specialty;

//    private List<String> vehicleVinIds;
//    private List<String> vehicleTypeIds;
//    private List<String> technicianIds;
}
