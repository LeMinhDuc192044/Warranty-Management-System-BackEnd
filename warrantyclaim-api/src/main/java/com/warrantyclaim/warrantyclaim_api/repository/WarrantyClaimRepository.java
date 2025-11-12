package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.WarrantyClaim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyClaimRepository extends JpaRepository<WarrantyClaim, String> {

    public List<WarrantyClaim> findByCustomerName(String customerName);

    @Query("SELECT wc FROM WarrantyClaim wc LEFT JOIN FETCH wc.vehicle")
    Page<WarrantyClaim> findAllWithVehicle(Pageable pageable);

}