package com.mhk.digidoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class ReportTemplateSegment  {

    private String segmentUUID;  // Unique identifier for the segment

    private String category;  // Category of the report template segment

    private String title;  // Title of the report template segment

    private String webContent;  // Web content of the report template segment

    private String textContent;  // Text content of the report template segment

    @JsonIgnore
    private ReportTemplate reportTemplate;  // The associated report template for this segment

    private int segmentOrder;

    public void generateUUID() {
        if (segmentUUID == null || segmentUUID.isEmpty()) {
            this.segmentUUID = UUID.randomUUID().toString();  // Generate UUID if not present
        }
    }

    public String getSegmentUUID() {
        return segmentUUID;
    }

    public void setSegmentUUID(String segmentUUID) {
        this.segmentUUID = segmentUUID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebContent() {
        return webContent;
    }

    public void setWebContent(String webContent) {
        this.webContent = webContent;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public ReportTemplate getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(ReportTemplate reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public int getSegmentOrder() {
        return segmentOrder;
    }

    public void setSegmentOrder(int segmentOrder) {
        this.segmentOrder = segmentOrder;
    }
}