package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleType;

public class SparePartInfoEvmDTO {
    private String id;
    private String name;
    private VehicleType vehicleType;
    private PartStatus condition;
}
