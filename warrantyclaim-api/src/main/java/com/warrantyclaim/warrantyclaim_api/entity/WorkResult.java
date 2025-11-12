package com.warrantyclaim.warrantyclaim_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity lưu trữ kết quả công việc bảo hành
 * Được tạo khi kỹ thuật viên hoàn thành công việc
 */
@Entity
@Table(name = "work_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkResult {

    @Id
    @Column(name = "claim_id", length = 50)
    private String claimId;

    @Column(name = "parts_used", columnDefinition = "TEXT")
    private String partsUsed; // Comma-separated list of part IDs

    @Column(name = "serial_numbers", columnDefinition = "TEXT")
    private String serialNumbers; // Comma-separated list of serial numbers

    @Column(name = "completion_notes", columnDefinition = "TEXT", nullable = false)
    private String completionNotes;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "completed_by_technician_id", length = 50)
    private String completedByTechnicianId;

    @Column(name = "work_duration_hours")
    private Integer workDurationHours; // Số giờ làm việc

    @Column(name = "customer_satisfaction_rating")
    private Integer customerSatisfactionRating; // 1-5 stars

    // Relationship với WarrantyClaim
    @OneToOne
    @JoinColumn(name = "claim_id", referencedColumnName = "claimId", insertable = false, updatable = false)
    private WarrantyClaim warrantyClaim;
}
