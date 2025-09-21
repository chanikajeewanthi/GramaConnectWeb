package org.example.gramaconnectweb.Backend.repository;

import org.example.gramaconnectweb.Backend.entity.CropReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropReportRepository extends JpaRepository<CropReport, Long> {
    List<CropReport> findByReportedBy(String email);
}
