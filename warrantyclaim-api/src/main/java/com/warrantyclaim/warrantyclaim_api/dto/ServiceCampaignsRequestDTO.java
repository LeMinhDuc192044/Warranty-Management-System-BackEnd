package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.ServiceCampaignsStatus;
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
public class ServiceCampaignsRequestDTO {

    @NotBlank(message = "Campaign type name cannot be blank!!")
    private String typeName;

    @NotNull(message = "Start date is required!!")
    private LocalDate startDate;

    @NotNull(message = "End date is required!!")
    private LocalDate endDate;

    @NotNull(message = "Specialty is needed!!!")
    private Specialty specialty;

    private String requiredParts;
    private String description;
    private Boolean NotificationSent = false;

    @NotNull(message = "Status is required!!")
    private ServiceCampaignsStatus status;

    private List<String> vehicleTypeIds;
    private List<String> technicianIds;

}
