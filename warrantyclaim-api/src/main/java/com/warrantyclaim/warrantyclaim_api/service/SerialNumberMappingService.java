package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.SerialNumberMappingRequestDTO;
import com.warrantyclaim.warrantyclaim_api.dto.SerialNumberMappingResponseDTO;

import java.util.List;

public interface SerialNumberMappingService {

    /**
     * Map serial number của phụ tùng vào VIN
     */
    SerialNumberMappingResponseDTO mapSerialToVehicle(SerialNumberMappingRequestDTO requestDTO);

    /**
     * Lấy tất cả serial numbers đã map cho một VIN
     */
    List<SerialNumberMappingResponseDTO> getSerialNumbersByVIN(String vin);

    /**
     * Lấy tất cả serial numbers cho một claim
     */
    List<SerialNumberMappingResponseDTO> getSerialNumbersByClaim(String claimId);

    /**
     * Kiểm tra serial number đã được sử dụng chưa
     */
    boolean isSerialNumberUsed(String serialNumber);

    /**
     * Lấy thông tin chi tiết của một serial number
     */
    SerialNumberMappingResponseDTO getSerialNumberDetails(String serialNumber);

    /**
     * Xóa mapping (chỉ admin/technician)
     */
    void deleteMapping(String serialNumber);

    /**
     * Cập nhật durability percentage
     */
    SerialNumberMappingResponseDTO updateDurability(String serialNumber, Integer durabilityPercentage);
}
