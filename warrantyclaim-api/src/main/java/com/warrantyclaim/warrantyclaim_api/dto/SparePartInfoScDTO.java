package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SparePartInfoScDTO {
    private String id;
    private String name;
    private VehicleType vehicleType;
    private PartStatus condition;
    private OfficeBranch officeBranch;
}