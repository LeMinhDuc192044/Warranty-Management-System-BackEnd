package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.RecallSCTechnician;
import com.warrantyclaim.warrantyclaim_api.entity.RecallSCTechnicianId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecallSCTechnicianRepository extends JpaRepository<RecallSCTechnician, RecallSCTechnicianId> {
    List<RecallSCTechnician> findByRecallId(String recallId);
    // Hoặc nếu muốn lấy luôn các Recall object
    @Query("SELECT r.recall FROM RecallSCTechnician r WHERE r.technicianId = :technicianId")
    List<com.warrantyclaim.warrantyclaim_api.entity.Recall> findRecallsByTechnicianId(@Param("technicianId") String technicianId);
}
