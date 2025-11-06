package com.warrantyclaim.warrantyclaim_api.repository;

import com.warrantyclaim.warrantyclaim_api.entity.Recall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecallRepository extends JpaRepository<Recall, String> {

}
