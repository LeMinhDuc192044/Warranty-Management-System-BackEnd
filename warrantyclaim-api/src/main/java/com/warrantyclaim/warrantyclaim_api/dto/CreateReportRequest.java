package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.Data;

@Data
public class CreateReportRequest {
    private String title;
    private String description;
    private String image;
    private String error;
//    private Integer amountOfCar;
}
