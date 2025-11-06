package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.RecallStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecallByTechnicianDTO {
    private String recallId;
    private String title;
    private String description;
    private RecallStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean notificationSent;
    private Integer totalVehicles;      // Tổng số vehicles trong recall
    private Integer totalVehicleTypes;  // Tổng số vehicle types
    private String severity;            // Mức độ nghiêm trọng (nếu có)
}