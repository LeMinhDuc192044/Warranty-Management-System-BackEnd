package com.warrantyclaim.warrantyclaim_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Thông tin xe còn bảo hành")
public class VehicleWarrantyStatusDTO {

    @Schema(description = "Mã VIN của xe", example = "VF8ABC12345678901")
    private String vin;

    @Schema(description = "Tên xe", example = "VinFast VF8")
    private String vehicleName;

    @Schema(description = "Ngày mua xe", example = "2023-05-01")
    private LocalDate purchaseDate;

    @Schema(description = "Ngày hết hạn bảo hành", example = "2025-05-01")
    private LocalDate warrantyEndDate;

    @Schema(description = "Xe còn bảo hành hay không", example = "true")
    private boolean underWarranty;
}
