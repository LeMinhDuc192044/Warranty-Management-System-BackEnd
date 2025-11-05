package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.Recall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecallRepository extends JpaRepository<Recall, String> {
    Page<Recall> findByTargetDistrictContaining(String district, Pageable pageable);
}
