package com.warrantyclaim.warrantyclaim_api.dto;

import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleStatus;
import com.warrantyclaim.warrantyclaim_api.enums.VehicleType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsSparePartsSCRequest {
//
//    @NotBlank(message = "Product ID is required")
//    @Size(max = 50, message = "Product ID must not exceed 50 characters")
//    @Pattern(
//            regexp = "^[A-Z]{3,5}-VF[3-9]-\\d{5,6}$",
//            message = "Part Number không đúng định dạng! VD: BAT-VF8-000123"
//    )
//    private String id;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Need vehicle to make ID")
    private VehicleType vehicleType;

    private PartStatus condition;

    @NotNull(message = "Need office branch to assign to!!!")
    private OfficeBranch officeBranch;

    private String partTypeId;
    private String vehicleVinId;
}
