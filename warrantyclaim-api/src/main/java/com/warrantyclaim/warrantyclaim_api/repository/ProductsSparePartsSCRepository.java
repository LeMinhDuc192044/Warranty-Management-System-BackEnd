package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.dto.*;
import com.warrantyclaim.warrantyclaim_api.entity.ElectricVehicle;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsEVM;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsSC;
import com.warrantyclaim.warrantyclaim_api.enums.OfficeBranch;
import com.warrantyclaim.warrantyclaim_api.enums.PartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductsSparePartsSCRepository extends JpaRepository<ProductsSparePartsSC, String> {
    List<ProductsSparePartsSC> findByNameContainingIgnoreCase(String name);
    List<ProductsSparePartsSC> findByPartType_Id(String partTypeId);

    @Query("""
    SELECT new com.warrantyclaim.warrantyclaim_api.dto.PartTypeCountResponse(
                                                                   p.officeBranch,
                                                                   p.partType.id,
                                                                   COUNT(p)
                                                               )
                                                               FROM ProductsSparePartsSC p
                                                               WHERE p.officeBranch = :branch
                                                               AND p.partType.id = :partTypeId
                                                               GROUP BY p.officeBranch, p.partType.id
""")
    List<PartTypeCountResponse> countByBranchAndPartType(@Param("branch") OfficeBranch branch,
                                                         @Param("partTypeId") String partTypeId);


    @Query("""
            SELECT new com.warrantyclaim.warrantyclaim_api.dto.SparePartInfoScDTO(
                    p.id,
                    p.name,
                    p.vehicleType,
                    p.condition,
                    p.officeBranch
                )
                FROM ProductsSparePartsSC p
                WHERE p.officeBranch = :branch
            """)
    List<SparePartInfoScDTO> findByOfficeBranch(@Param("branch") OfficeBranch officeBranch);

    @Query("""        
        SELECT new com.warrantyclaim.warrantyclaim_api.dto.PartTypeAndPartStatusCountEVMResponse(
            p.partType.id,
            COUNT(p),
            p.condition
        )
        FROM ProductsSparePartsSC p
        WHERE p.partType.id = :partTypeId
        AND p.condition = :conditions
        GROUP BY p.partType.id, p.condition
        """)
    List<PartTypeAndPartStatusCountEVMResponse> countByTypeAndCondition(
            @Param("partTypeId") String partTypeId,
            @Param("conditions") PartStatus conditions
    );

    List<ProductsSparePartsSC> findByElectricVehicleAndPartType_Id(
            ElectricVehicle vehicle,
            String partTypeId
    );

}
