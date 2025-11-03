package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleBasicInfoDTO {

    private String vehicleId;
    private String vehicleName;
    private String owner;
    private String email;
    private String phoneNumber;
    private String model;
    private String picture;
}
