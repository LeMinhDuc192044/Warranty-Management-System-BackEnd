package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.SerialNumberMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SerialNumberMappingRepository extends JpaRepository<SerialNumberMapping, String> {

    // Tìm tất cả serial numbers theo VIN
    List<SerialNumberMapping> findByVehicleVIN(String vehicleVIN);

    // Tìm tất cả serial numbers theo Claim ID
    List<SerialNumberMapping> findByClaimId(String claimId);

    // Tìm tất cả serial numbers theo Part ID
    List<SerialNumberMapping> findByPartId(String partId);

    // Kiểm tra serial number đã được map chưa
    boolean existsBySerialNumber(String serialNumber);

    // Tìm mapping theo serial number
    Optional<SerialNumberMapping> findBySerialNumber(String serialNumber);

    // Tìm tất cả mappings của một technician
    List<SerialNumberMapping> findByMappedByTechnicianId(String technicianId);

    // Đếm số lượng parts đã map cho một VIN
    @Query("SELECT COUNT(s) FROM SerialNumberMapping s WHERE s.vehicleVIN = :vin")
    Long countByVehicleVIN(@Param("vin") String vin);

    // Lấy danh sách serial numbers theo claim với thông tin chi tiết
    @Query("""
                SELECT s FROM SerialNumberMapping s
                LEFT JOIN FETCH s.sparePart
                WHERE s.claimId = :claimId
                ORDER BY s.mappingDate DESC
            """)
    List<SerialNumberMapping> findByClaimIdWithDetails(@Param("claimId") String claimId);
}
