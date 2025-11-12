package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnicianWorkStatsDTO {

    private String technicianId;
    private String technicianName;
    private Integer totalWorksCompleted;
    private Integer totalWorkHours;
    private Double averageRating;
    private Integer highRatedWorks; // Số công việc rating >= 4
}
