package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeEVM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsSparePartsTypeEVMRepository extends JpaRepository<ProductsSparePartsTypeEVM, String> {

    /**
     * Get all unique part names for category filter
     */
    @Query("SELECT DISTINCT p.partName FROM ProductsSparePartsTypeEVM p WHERE p.partName IS NOT NULL ORDER BY p.partName")
    List<String> findDistinctPartNames();
}
