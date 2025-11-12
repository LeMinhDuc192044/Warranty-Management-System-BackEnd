package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.PartAvailabilityItemDTO;
import com.warrantyclaim.warrantyclaim_api.dto.PartsAvailabilityResponseDTO;

import java.util.List;

public interface PartsInventoryService {

    /**
     * Kiểm tra tình trạng có sẵn của phụ tùng cho một warranty claim
     * 
     * @param claimId ID của warranty claim
     * @return Thông tin chi tiết về tình trạng phụ tùng
     */
    PartsAvailabilityResponseDTO checkPartsAvailability(String claimId);

    /**
     * Kiểm tra tình trạng có sẵn của danh sách part types tại một chi nhánh
     * 
     * @param partTypeIds  Danh sách ID loại phụ tùng
     * @param officeBranch Chi nhánh cần kiểm tra
     * @return Danh sách thông tin tình trạng từng phụ tùng
     */
    List<PartAvailabilityItemDTO> checkPartTypeAvailability(List<String> partTypeIds, String officeBranch);

    /**
     * Lấy số lượng phụ tùng available tại chi nhánh
     * 
     * @param partTypeId   ID loại phụ tùng
     * @param officeBranch Chi nhánh
     * @return Số lượng available
     */
    Integer getAvailableQuantity(String partTypeId, String officeBranch);
}
