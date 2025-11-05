package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.EvmApprovalStatus;
import com.warrantyclaim.warrantyclaim_api.enums.RecallStatus;
import com.warrantyclaim.warrantyclaim_api.enums.Specialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecallsListDTO {

    private String id;
    private String name;
    private String issueDescription;

    private LocalDate startDate;

    private String requiredAction;
    private String partsRequired;
    private Specialty specialty;

    private RecallStatus status;

    private Boolean notificationSent;

    private EvmApprovalStatus evmApprovalStatus;


    private Integer vehicleTypeCount;
    private Integer technicianCount;
    private Integer vehicleCount;
}
