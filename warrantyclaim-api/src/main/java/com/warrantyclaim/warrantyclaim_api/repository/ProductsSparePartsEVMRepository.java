package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.dto.PartTypeCountEVMResponse;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsEVM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProductsSparePartsEVMRepository extends JpaRepository<ProductsSparePartsEVM, String> {
    List<ProductsSparePartsEVM> findByNameContainingIgnoreCase(String name);

    @Query("""        
            SELECT new com.warrantyclaim.warrantyclaim_api.dto.PartTypeCountEVMResponse(
                p.partType.id,
                COUNT(p)
                )
                FROM ProductsSparePartsEVM p
                WHERE p.partType.id = :partTypeId
                GROUP BY p.partType.id
            """)
    List<PartTypeCountEVMResponse> countByType(@Param("partTypeId") String partTypeId);
}
