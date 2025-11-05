package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.EVMStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EVMStaffRepository extends JpaRepository<EVMStaff, String> {

    Optional<EVMStaff> findByEmail(String email);
    void deleteByEmail(String email);

}
