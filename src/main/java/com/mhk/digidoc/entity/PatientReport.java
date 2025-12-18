package com.mhk.digidoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.List;
import java.util.UUID;




public class PatientReport {

    private String reportInstanceUUID;  // Unique identifier for the report instance

    private String reportName;  // Name of the report

    private String reportID;  // Identifier for the report, can be used to link to a template or specific report type

    /**This is used weather this requires approval from doctor or nurse can approve. e.g. Vital Observation does not require approval. */
    private String approvalCategory; // Approval category of the report template


    private Date reportStartDate;  // Start date of the report

    private Date reportEndDate; // Start date of the report

    private String reportStatus;  // Status of the report

    private String assignedTo;  // Person assigned to the report

    private String signedBy;  // Person who signs the report

    private String referredBy;  // Person who referred the patient

    private Date lastUpdatedTime;  // Last updated time of the report

    private String remarks;

    private String templateRererenceID;

    private String department;  // Department handling the report

    @JsonIgnore
    private Patient patient;  // Patient associated with the report

    private String storageLocation;

    private String reportOpenStatus;

    private String openedBy;


    public String getReportOpenStatus() {
        return reportOpenStatus;
    }

    public void setReportOpenStatus(String reportOpenStatus) {
        this.reportOpenStatus = reportOpenStatus;
    }

    public String getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(String openedBy) {
        this.openedBy = openedBy;
    }

    public String getApprovalCategory() {
        return approvalCategory;
    }

    public void setApprovalCategory(String approvalCategory) {
        this.approvalCategory = approvalCategory;
    }


    private List<PatientReportSegment> segments;  // List of segments associated with the report

    public void generateUUID() {
        if (reportInstanceUUID == null) {
            this.reportInstanceUUID = UUID.randomUUID().toString();  // Generate UUID if not present
        }
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }


    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public Date getReportEndDate() {
        return reportEndDate;
    }

    public void setReportEndDate(Date reportEndDate) {
        this.reportEndDate = reportEndDate;
    }
    public String getReportInstanceUUID() {
        return reportInstanceUUID;
    }

    public void setReportInstanceUUID(String reportInstanceUUID) {
        this.reportInstanceUUID = reportInstanceUUID;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Date getReportStartDate() {
        return reportStartDate;
    }

    public void setReportStartDate(Date reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTemplateRererenceID() {
        return templateRererenceID;
    }

    public void setTemplateRererenceID(String templateRererenceID) {
        this.templateRererenceID = templateRererenceID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<PatientReportSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<PatientReportSegment> segments) {
        this.segments = segments;
    }

}