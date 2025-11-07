package com.warrantyclaim.warrantyclaim_api.repository;


import com.warrantyclaim.warrantyclaim_api.entity.WarrantyClaim;
import com.warrantyclaim.warrantyclaim_api.enums.WarrantyClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarrantyClaimRepository extends JpaRepository<WarrantyClaim, String> {

    public List<WarrantyClaim> findByCustomerName(String customerName);

    @Query("SELECT wc.officeBranch, COUNT(wc) " +
            "FROM WarrantyClaim wc " +
            "WHERE wc.status IN (:statuses) AND wc.officeBranch IS NOT NULL " +
            "GROUP BY wc.officeBranch")
    List<Object[]> getClaimCountByOfficeBranch(@Param("statuses") List<WarrantyClaimStatus> statuses);
    long countByStatusIn(List<WarrantyClaimStatus> statuses);


}