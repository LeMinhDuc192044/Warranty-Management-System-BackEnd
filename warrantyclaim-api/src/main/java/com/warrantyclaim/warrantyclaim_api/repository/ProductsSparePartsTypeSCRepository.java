package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeSC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsSparePartsTypeSCRepository extends JpaRepository<ProductsSparePartsTypeSC, String> {

    /**
     * Get all unique part names for category filter
     */
    @Query("SELECT DISTINCT p.partName FROM ProductsSparePartsTypeSC p WHERE p.partName IS NOT NULL ORDER BY p.partName")
    List<String> findDistinctPartNames();
}
