package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.dto.WarrantyPartDTO;
import com.warrantyclaim.warrantyclaim_api.entity.ProductsSparePartsTypeSC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsSparePartsTypeSCRepository extends JpaRepository<ProductsSparePartsTypeSC, String> {

    /**
     * Find all parts by branch office (district)
     */
    Page<ProductsSparePartsTypeSC> findByBranchOffice(String branchOffice, Pageable pageable);

    /**
     * Find specific part by EVM part type ID and branch office
     */
    Optional<ProductsSparePartsTypeSC> findByPartTypeIdEVMAndBranchOffice(String partTypeIdEVM, String branchOffice);

    /**
     * Find all parts with low stock or out of stock at a branch
     */
    @Query("SELECT p FROM ProductsSparePartsTypeSC p WHERE p.branchOffice = :branchOffice AND (p.stockStatus = 'LOW_STOCK' OR p.stockStatus = 'OUT_OF_STOCK')")
    List<ProductsSparePartsTypeSC> findLowStockPartsByBranch(@Param("branchOffice") String branchOffice);

    /**
     * Get all unique part names for category filter
     */
    @Query("SELECT DISTINCT p.partName FROM ProductsSparePartsTypeSC p WHERE p.partName IS NOT NULL ORDER BY p.partName")
    List<String> findDistinctPartNames();

    /**
     * Get warranty parts for a vehicle type based on warranty policies
     * This query joins:
     * 1. Electric Vehicle Type -> Warranty Policy (via
     * Warranty_Policy_Electric_Vehicle_Type)
     * 2. Warranty Policy -> SC Parts (via
     * Warranty_Policy_Products_Spare_Parts_Type_SC)
     * 3. SC Parts details from Products_Spare_Parts_Type_SC
     */
    @Query(value = "SELECT DISTINCT " +
            "pspt.ID_Products_Part_Type_SC AS partTypeId, " +
            "pspt.Part_Name AS partName, " +
            "pspt.Description AS description, " +
            "pspt.Total_Amount_Of_Product AS stock, " +
            "pspt.Price AS price, " +
            "pspt.Manufacturer AS manufacturer, " +
            "pspt.`Condition` AS `condition`, " +
            "wp.Description AS warrantyPolicyName, " +
            "wp.ID_Warranty_Policy AS warrantyPolicyId " +
            "FROM Products_Spare_Parts_Type_SC pspt " +
            "JOIN Warranty_Policy_Products_Spare_Parts_Type_SC wpp " +
            "  ON pspt.ID_Products_Part_Type_SC = wpp.ID_Products_Part_Type_SC " +
            "JOIN Warranty_Policy wp " +
            "  ON wpp.ID_Warranty_Policy = wp.ID_Warranty_Policy " +
            "JOIN Warranty_Policy_Electric_Vehicle_Type wpevt " +
            "  ON wp.ID_Warranty_Policy = wpevt.ID_Warranty_Policy " +
            "WHERE wpevt.ID_Electric_Vehicle_Type = :vehicleTypeId " +
            "ORDER BY pspt.Part_Name", nativeQuery = true)
    List<Object[]> findWarrantyPartsByVehicleTypeRaw(@Param("vehicleTypeId") String vehicleTypeId);

    /**
     * Default method to convert raw query results to DTO
     */
    default List<WarrantyPartDTO> findWarrantyPartsByVehicleType(String vehicleTypeId) {
        List<Object[]> results = findWarrantyPartsByVehicleTypeRaw(vehicleTypeId);
        return results.stream().map(row -> new WarrantyPartDTO(
                (String) row[0], // partTypeId
                (String) row[1], // partName
                (String) row[2], // description
                row[3] != null ? ((Number) row[3]).intValue() : 0, // stock
                row[4] != null ? ((Number) row[4]).doubleValue() : 0.0, // price
                (String) row[5], // manufacturer
                (String) row[6], // condition
                (String) row[7], // warrantyPolicyName
                (String) row[8] // warrantyPolicyId
        )).collect(java.util.stream.Collectors.toList());
    }
}
