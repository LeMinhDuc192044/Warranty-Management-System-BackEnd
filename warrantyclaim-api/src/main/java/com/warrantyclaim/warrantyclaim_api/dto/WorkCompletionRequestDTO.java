package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkCompletionRequestDTO {

    private String claimId;

    private List<String> partsUsed; // List of part IDs

    private List<String> serialNumbers; // List of serial numbers used

    private String completionNotes;

    private LocalDateTime returnDate;

    private String completedByTechnicianId;

    private Integer workDurationHours; // Optional - số giờ làm việc
}
