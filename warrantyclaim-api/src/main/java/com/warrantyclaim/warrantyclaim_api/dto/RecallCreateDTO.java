package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.EvmApprovalStatus;
import com.warrantyclaim.warrantyclaim_api.enums.RecallStatus;
import com.warrantyclaim.warrantyclaim_api.enums.Specialty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecallCreateDTO {

    @NotBlank(message = "Needed name for recall!!!")
    private String name;

    @NotBlank(message = "Need to describe issue to solve!!!")
    private String issueDescription;

    @NotNull(message = "Start date is required for recall!!!")
    private LocalDate startDate;

    @NotBlank(message = "What need to be done for fixing!!!")
    private String requiredAction;

    @NotBlank(message = "Part needed to fix the issue!!!")
    private String partsRequired;

    @NotNull(message = "Specialty is needed!!!")
    private Specialty specialty;

    private RecallStatus status;

    private Boolean notificationSent;

    private EvmApprovalStatus evmApprovalStatus;

    @NotNull(message = "Need vehicle type is required!!!")
    private List<String> vehicleTypeIds;// Gắn loại xe vào chiến dịch



}

