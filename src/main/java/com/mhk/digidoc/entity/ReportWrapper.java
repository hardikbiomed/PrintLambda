package com.mhk.digidoc.entity;

public class ReportWrapper {

    boolean isInProgress;

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }

    public String getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(String reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    private String reportTemplate;

}
