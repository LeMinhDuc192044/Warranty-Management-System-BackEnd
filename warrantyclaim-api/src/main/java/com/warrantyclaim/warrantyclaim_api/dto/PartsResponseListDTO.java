package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartsRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartsResponseListDTO {

    private String id;
//    private String partNumber;
    private String partName;
    private Integer quantity;
    private LocalDate requestDate;
    private PartsRequestStatus status;
    private OfficeBranch officeBranch;

    // Simplified nested info
    private String partTypeName;
    private VehicleBasicInfoDTO vehicle;

//    private String staffName;
}
