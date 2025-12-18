package com.mhk.digidoc.entity;



import java.util.List;
import java.util.UUID;

public class ReportTemplate  {

    private String reporttemplateUUID;  // Unique identifier for the report template

    private String name;  // Name of the report template

    private List<ReportTemplateSegment> reportTemplateSegments;  // List of segments associated with the template

    private String createdOn;  // Date when the report template was created

    private String createdBy;  // User who created the report template

    private String department; // Department associated with the report template

    /**This is used weather this requires approval from doctor or nurse can approve. e.g. Vital Observation does not require approval. */
    private String approvalCategory; // Approval category of the report template

    private int version;

    public void generateUUID() {
        if (reporttemplateUUID == null || reporttemplateUUID.isEmpty()) {
            this.reporttemplateUUID = UUID.randomUUID().toString();  // Generate UUID if not present
        }
    }

    public String getApprovalCategory() {
        return approvalCategory;
    }

    public void setApprovalCategory(String approvalCategory) {
        this.approvalCategory = approvalCategory;
    }
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getReporttemplateUUID() {
        return reporttemplateUUID;
    }

    public void setReporttemplateUUID(String reporttemplateUUID) {
        this.reporttemplateUUID = reporttemplateUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReportTemplateSegment> getReportTemplateSegments() {
        return reportTemplateSegments;
    }

    public void setReportTemplateSegments(List<ReportTemplateSegment> reportTemplateSegments) {
        this.reportTemplateSegments = reportTemplateSegments;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}