package com.warrantyclaim.warrantyclaim_api.service;

import com.warrantyclaim.warrantyclaim_api.dto.TechnicianWorkStatsDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WorkCompletionRequestDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WorkResultResponseDTO;

import java.util.List;

public interface WorkResultService {

    /**
     * Tạo WorkResult khi kỹ thuật viên hoàn thành công việc
     */
    WorkResultResponseDTO completeWork(WorkCompletionRequestDTO request);

    /**
     * Lấy WorkResult theo claimId
     */
    WorkResultResponseDTO getWorkResultByClaimId(String claimId);

    /**
     * Lấy tất cả WorkResult của một technician
     */
    List<WorkResultResponseDTO> getWorkResultsByTechnician(String technicianId);

    /**
     * Cập nhật customer satisfaction rating
     */
    WorkResultResponseDTO updateCustomerRating(String claimId, Integer rating);

    /**
     * Lấy thống kê công việc của technician
     */
    TechnicianWorkStatsDTO getTechnicianWorkStats(String technicianId);
}
