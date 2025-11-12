package com.warrantyclaim.warrantyclaim_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartsAvailabilityRequestDTO {
    private List<String> partTypeIds; // Danh sách ID loại phụ tùng cần kiểm tra
    private String officeBranch; // Chi nhánh cần kiểm tra
}
