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
@Schema(description = "Thông tin trạng thái bảo hành của xe điện")
public class WarrantyStatusDTO {

    @Schema(description = "Tên chính sách bảo hành", example = "Full Vehicle Warranty")
    private String policyName;

    @Schema(description = "Loại bảo hành", example = "BATTERY")
    private String coverageType;

    @Schema(description = "Ngày kết thúc bảo hành", example = "2033-11-01")
    private LocalDate warrantyEndDate;

    @Schema(description = "Xe còn trong thời hạn bảo hành hay không", example = "true")
    private boolean underWarranty;
}
