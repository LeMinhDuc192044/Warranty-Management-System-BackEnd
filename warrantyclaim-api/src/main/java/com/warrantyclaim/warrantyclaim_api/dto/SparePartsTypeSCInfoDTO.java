package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparePartsTypeSCInfoDTO {
    private String id;
    private String partName;
    private String manufacturer;
    private Integer yearModelYear;
    private Float price;
}
