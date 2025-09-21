package org.example.gramaconnectweb.Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor

@Table(name = "crop_report")
public class CropReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String issueType;
    private String cropType;
    private String description;
    private String photoUrl;  // store uploaded file path or URL

    private String reportedBy; // email of the user
    private LocalDateTime reportedAt = LocalDateTime.now();

    public CropReport(Long id, String issueType, String cropType, String description, String photoUrl, String reportedBy, LocalDateTime reportedAt) {
        this.id = id;
        this.issueType = issueType;
        this.cropType = cropType;
        this.description = description;
        this.photoUrl = photoUrl;
        this.reportedBy = reportedBy;
        this.reportedAt = reportedAt;
    }

    public Long getId() {
        return id;
    }

    public String getIssueType() {
        return issueType;
    }

    public String getCropType() {
        return cropType;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }
}
