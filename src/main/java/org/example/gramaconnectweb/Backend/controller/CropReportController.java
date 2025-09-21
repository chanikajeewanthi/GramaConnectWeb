//package org.example.gramaconnectweb.Backend.controller;
//
//import org.example.gramaconnectweb.Backend.entity.CropReport;
//import org.example.gramaconnectweb.Backend.service.CropReportService;
//import org.example.gramaconnectweb.Backend.service.JwtUtil;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/reports")
//public class CropReportController {
//
//    private final CropReportService service;
//    private final JwtUtil jwtUtil;
//
//    public CropReportController(CropReportService service, JwtUtil jwtUtil) {
//        this.service = service;
//        this.jwtUtil = jwtUtil;
//    }
//
//    // CREATE a new crop report
////    @PostMapping
////    public ResponseEntity<?> submitReport(@RequestHeader("Authorization") String authHeader,
////                                          @RequestBody CropReport report) {
////        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
////            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
////        }
////        String token = authHeader.substring(7);
////        String email;
////        try {
////            email = jwtUtil.extractEmail(token);
////        } catch (Exception e) {
////            return ResponseEntity.status(401).body("Invalid JWT token");
////        }
////
////        report.setReportedBy(email);
////        CropReport saved = service.saveReport(report);
////        return ResponseEntity.ok(saved);
////    }
//    @PostMapping
//    public ResponseEntity<?> submitReport(@RequestHeader("Authorization") String authHeader,
//                                          @RequestBody CropReport report) {
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
//        }
//        String token = authHeader.substring(7);
//        String email;
//        try {
//            email = jwtUtil.extractEmail(token);
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Invalid JWT token");
//        }
//
//        // Check role (only FARMER can submit)
//        List<String> roles = jwtUtil.extractRoles(token);
//        if (!roles.contains("ROLE_FARMER")) {
//            return ResponseEntity.status(403).body("Only farmers can report issues");
//        }
//
//        report.setReportedBy(email);
//        CropReport saved = service.saveReport(report);
//        return ResponseEntity.ok(saved);
//    }
//
//    // GET reports for logged-in user
//    @GetMapping("/my")
//    public ResponseEntity<?> getMyReports(@RequestHeader("Authorization") String authHeader) {
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
//        }
//        String token = authHeader.substring(7);
//        String email;
//        try {
//            email = jwtUtil.extractEmail(token);
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Invalid JWT token");
//        }
//
//        List<CropReport> reports = service.getReportsByUser(email);
//        return ResponseEntity.ok(reports);
//    }
//
//    // GET all reports (admin)
//    @GetMapping("/all")
//    public List<CropReport> getAllReports() {
//        return service.getAllReports();
//    }
//    // UPDATE a report (only by the user who created it)
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateReport(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable Long id,
//            @RequestBody CropReport updatedReport) {
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
//        }
//        String token = authHeader.substring(7);
//        String email;
//        try {
//            email = jwtUtil.extractEmail(token);
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Invalid JWT token");
//        }
//
//        // Only farmer role can update
//        List<String> roles = jwtUtil.extractRoles(token);
//        if (!roles.contains("ROLE_FARMER")) {
//            return ResponseEntity.status(403).body("Only farmers can update reports");
//        }
//
//        CropReport existingReport = service.getReportById(id);
//        if (existingReport == null) {
//            return ResponseEntity.status(404).body("Report not found");
//        }
//
//        // Check ownership
//        if (!existingReport.getReportedBy().equals(email)) {
//            return ResponseEntity.status(403).body("You can only update your own reports");
//        }
//
//        // Update fields
//        existingReport.setIssueType(updatedReport.getIssueType());
//        existingReport.setCropType(updatedReport.getCropType());
//        existingReport.setDescription(updatedReport.getDescription());
//        existingReport.setPhotoUrl(updatedReport.getPhotoUrl());
//
//        CropReport saved = service.saveReport(existingReport);
//        return ResponseEntity.ok(saved);
//    }
//
//
//    // DELETE a report
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteReport(
//            @RequestHeader("Authorization") String authHeader,
//            @PathVariable Long id) {
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
//        }
//        String token = authHeader.substring(7);
//        String email;
//        try {
//            email = jwtUtil.extractEmail(token);
//        } catch (Exception e) {
//            return ResponseEntity.status(401).body("Invalid JWT token");
//        }
//
//        CropReport existingReport = service.getReportById(id);
//        if (existingReport == null) {
//            return ResponseEntity.status(404).body("Report not found");
//        }
//
//        // Check ownership
//        if (!existingReport.getReportedBy().equals(email)) {
//            return ResponseEntity.status(403).body("You can only delete your own reports");
//        }
//
//        service.deleteReport(id);
//        return ResponseEntity.ok("Report deleted successfully");
//    }
//
//}
package org.example.gramaconnectweb.Backend.controller;

import org.example.gramaconnectweb.Backend.entity.CropReport;
import org.example.gramaconnectweb.Backend.service.CropReportService;
import org.example.gramaconnectweb.Backend.service.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class CropReportController {

    private final CropReportService service;
    private final JwtUtil jwtUtil;

    public CropReportController(CropReportService service, JwtUtil jwtUtil) {
        this.service = service;
        this.jwtUtil = jwtUtil;
    }

    // CREATE a new crop report
    @PostMapping
    public ResponseEntity<?> submitReport(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody CropReport report) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid JWT token");
        }

        // Only FARMER role can submit
        List<String> roles = jwtUtil.extractRoles(token);
        if (!roles.contains("ROLE_FARMER")) {
            return ResponseEntity.status(403).body("Only farmers can report issues");
        }

        report.setReportedBy(email);
        CropReport saved = service.saveReport(report);
        return ResponseEntity.ok(saved);
    }

    // GET reports for logged-in user (default GET /api/reports)
    @GetMapping
    public ResponseEntity<?> getReports(@RequestHeader("Authorization") String authHeader) {
        // same logic as getMyReports()
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        List<CropReport> reports = service.getReportsByUser(email);
        return ResponseEntity.ok(reports);
    }


    // GET all reports (admin)
    @GetMapping("/all")
    public List<CropReport> getAllReports() {
        return service.getAllReports();
    }

    // UPDATE a report (only by the user who created it)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReport(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestBody CropReport updatedReport) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid JWT token");
        }

        List<String> roles = jwtUtil.extractRoles(token);
        if (!roles.contains("ROLE_FARMER")) {
            return ResponseEntity.status(403).body("Only farmers can update reports");
        }

        CropReport existingReport = service.getReportById(id);
        if (existingReport == null) {
            return ResponseEntity.status(404).body("Report not found");
        }

        if (!existingReport.getReportedBy().equals(email)) {
            return ResponseEntity.status(403).body("You can only update your own reports");
        }

        existingReport.setIssueType(updatedReport.getIssueType());
        existingReport.setCropType(updatedReport.getCropType());
        existingReport.setDescription(updatedReport.getDescription());
        existingReport.setPhotoUrl(updatedReport.getPhotoUrl());

        CropReport saved = service.saveReport(existingReport);
        return ResponseEntity.ok(saved);
    }

    // DELETE a report
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid JWT token");
        }

        CropReport existingReport = service.getReportById(id);
        if (existingReport == null) {
            return ResponseEntity.status(404).body("Report not found");
        }

        if (!existingReport.getReportedBy().equals(email)) {
            return ResponseEntity.status(403).body("You can only delete your own reports");
        }

        service.deleteReport(id);
        return ResponseEntity.ok("Report deleted successfully");
    }

}

