package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkResultResponseDTO {

    private String claimId;

    private List<String> partsUsed;

    private List<String> serialNumbers;

    private String completionNotes;

    private LocalDateTime returnDate;

    private LocalDateTime completedAt;

    private String completedByTechnicianId;

    private String technicianName; // Tên kỹ thuật viên

    private Integer workDurationHours;

    private Integer customerSatisfactionRating;

    // Thông tin claim liên quan
    private String vehicleVIN;
    private String vehicleName;
    private String customerName;
}
