package org.example.gramaconnectweb.Backend.service;

import org.example.gramaconnectweb.Backend.entity.CropReport;
import org.example.gramaconnectweb.Backend.repository.CropReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CropReportService {

    private final CropReportRepository repository;

    public CropReportService(CropReportRepository repository) {
        this.repository = repository;
    }

    public CropReport saveReport(CropReport report) {
        return repository.save(report);
    }

    public List<CropReport> getReportsByUser(String email) {
        return repository.findByReportedBy(email);
    }

    public List<CropReport> getAllReports() {
        return repository.findAll();
    }

    public void deleteReport(Long id) {
        repository.deleteById(id);
    }

    public CropReport getReportById(Long id) {
        return repository.findById(id).orElse(null);
    }

}
