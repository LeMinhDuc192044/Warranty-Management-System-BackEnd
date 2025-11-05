package com.warrantyclaim.warrantyclaim_api.repository;


import com.warrantyclaim.warrantyclaim_api.entity.SCTechnician;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface SCTechnicianRepository extends JpaRepository<SCTechnician, String>
{
    Optional<SCTechnician> findByEmail(String email);
    void deleteByEmail(String email);


    Optional<SCTechnician> findByUserId(Long userId);

    boolean existsByEmail(String email);

    boolean existsByUserId(Long userId);

    Page<SCTechnician> findByBranchOffice(String branchOffice, Pageable pageable);

    Page<SCTechnician> findBySpecialty(String specialty, Pageable pageable);

    @Query("SELECT t FROM SCTechnician t WHERE " +
            "LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<SCTechnician> searchTechnicians(@Param("keyword") String keyword, Pageable pageable);
}
