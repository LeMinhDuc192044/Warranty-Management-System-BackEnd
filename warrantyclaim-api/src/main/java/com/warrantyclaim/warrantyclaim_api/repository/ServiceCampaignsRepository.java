package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.ServiceCampaigns;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceCampaignsRepository extends JpaRepository<ServiceCampaigns, String> {
    Page<ServiceCampaigns> findByTargetDistrictContaining(String district, Pageable pageable);
}
