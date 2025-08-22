package com.mhk.digidoc.entity;

public class ReportWrapper {

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientReport getPatientReport() {
        return patientReport;
    }




    public void setPatientReport(PatientReport patientReport) {
        this.patientReport = patientReport;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    private Patient patient;

    private PatientReport patientReport;

    private String reportName;

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    private String reportContent;

    public String getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(String reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    private String reportTemplate;

    private String tenant;
}
