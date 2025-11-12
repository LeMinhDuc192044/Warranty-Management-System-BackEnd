package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.WorkResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkResultRepository extends JpaRepository<WorkResult, String> {

    /**
     * Tìm WorkResult theo claimId
     */
    Optional<WorkResult> findByClaimId(String claimId);

    /**
     * Tìm tất cả WorkResult của một technician
     */
    List<WorkResult> findByCompletedByTechnicianId(String technicianId);

    /**
     * Kiểm tra xem claim đã có WorkResult chưa
     */
    boolean existsByClaimId(String claimId);

    /**
     * Lấy danh sách WorkResult với rating cao
     */
    @Query("SELECT w FROM WorkResult w WHERE w.customerSatisfactionRating >= :minRating ORDER BY w.completedAt DESC")
    List<WorkResult> findHighRatedWorks(Integer minRating);

    /**
     * Tính tổng số giờ làm việc của một technician
     */
    @Query("SELECT COALESCE(SUM(w.workDurationHours), 0) FROM WorkResult w WHERE w.completedByTechnicianId = :technicianId")
    Integer getTotalWorkHoursByTechnician(String technicianId);

    /**
     * Lấy rating trung bình của một technician
     */
    @Query("SELECT AVG(w.customerSatisfactionRating) FROM WorkResult w WHERE w.completedByTechnicianId = :technicianId AND w.customerSatisfactionRating IS NOT NULL")
    Double getAverageRatingByTechnician(String technicianId);
}
