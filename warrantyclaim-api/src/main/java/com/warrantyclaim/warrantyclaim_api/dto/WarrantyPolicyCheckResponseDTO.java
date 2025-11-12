package com.warrantyclaim.warrantyclaim_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyPolicyCheckResponseDTO {

    @JsonProperty("isEligible")
    private boolean isEligible; // Xe có đủ điều kiện bảo hành không

    private String message; // Thông báo cho user

    private List<WarrantyPolicyDetailDTO> applicablePolicies; // Các policy áp dụng được

    private String vehicleType; // Loại xe được check

    private LocalDate purchaseDate; // Ngày mua xe

    private Integer vehicleAgeMonths; // Tuổi xe tính bằng tháng

    private List<String> reasons; // Lý do không đủ điều kiện (nếu có)

    private List<String> additionalInfo; // Thông tin bổ sung về xe
}
