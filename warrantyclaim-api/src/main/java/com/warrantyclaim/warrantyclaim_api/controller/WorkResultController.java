package com.warrantyclaim.warrantyclaim_api.controller;

import com.warrantyclaim.warrantyclaim_api.dto.TechnicianWorkStatsDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WorkCompletionRequestDTO;
import com.warrantyclaim.warrantyclaim_api.dto.WorkResultResponseDTO;
import com.warrantyclaim.warrantyclaim_api.service.WorkResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Work Result management
 * Quản lý kết quả công việc bảo hành
 */
@RestController
@RequestMapping("/api/work-results")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class WorkResultController {

    private final WorkResultService workResultService;

    /**
     * POST /api/work-results/complete
     * Tạo work result khi kỹ thuật viên hoàn thành công việc
     */
    @PostMapping("/complete")
    public ResponseEntity<?> completeWork(@RequestBody WorkCompletionRequestDTO request) {
        try {
            WorkResultResponseDTO result = workResultService.completeWork(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error completing work: " + e.getMessage());
        }
    }

    /**
     * GET /api/work-results/claim/{claimId}
     * Lấy work result theo claimId
     */
    @GetMapping("/claim/{claimId}")
    public ResponseEntity<?> getWorkResultByClaimId(@PathVariable String claimId) {
        try {
            WorkResultResponseDTO result = workResultService.getWorkResultByClaimId(claimId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Work result not found for claim: " + claimId);
        }
    }

    /**
     * GET /api/work-results/technician/{technicianId}
     * Lấy tất cả work results của một technician
     */
    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<WorkResultResponseDTO>> getWorkResultsByTechnician(
            @PathVariable String technicianId) {
        List<WorkResultResponseDTO> results = workResultService.getWorkResultsByTechnician(technicianId);
        return ResponseEntity.ok(results);
    }

    /**
     * PATCH /api/work-results/{claimId}/rating?rating=5
     * Cập nhật customer satisfaction rating
     */
    @PatchMapping("/{claimId}/rating")
    public ResponseEntity<?> updateCustomerRating(
            @PathVariable String claimId,
            @RequestParam Integer rating) {
        try {
            WorkResultResponseDTO result = workResultService.updateCustomerRating(claimId, rating);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Work result not found for claim: " + claimId);
        }
    }

    /**
     * GET /api/work-results/technician/{technicianId}/stats
     * Lấy thống kê công việc của technician
     */
    @GetMapping("/technician/{technicianId}/stats")
    public ResponseEntity<TechnicianWorkStatsDTO> getTechnicianStats(
            @PathVariable String technicianId) {
        TechnicianWorkStatsDTO stats = workResultService.getTechnicianWorkStats(technicianId);
        return ResponseEntity.ok(stats);
    }
}
