package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EVMPartTypeDTO {
    private String id;
    private String partName;
    private String description;
    private Integer yearModelYear;
    private Integer totalAmountOfProduct;
    private Float price;
    private String manufacturer;
    private String stockStatus; // IN_STOCK, LOW_STOCK, OUT_OF_STOCK
}
