package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportInfoDTO {
    String reportId;
    String description;
    String image;
    String reportName;
//    Integer amountOfCar;
    String error;

}
