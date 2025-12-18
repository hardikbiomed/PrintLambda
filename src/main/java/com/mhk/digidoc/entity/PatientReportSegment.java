package com.mhk.digidoc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.UUID;


public class PatientReportSegment  {

    private String id;  // Unique identifier for the report segment

    private String category;  // Category of the report segment

    private String title;  // Title of the report segment

    private String webContent;  // Web content of the report segment

    private String textContent;  // Text content of the report segment

    private String referenceSegment;

    private String mandatory; // whether the segment is mandatory
    /*This is necessary to show UI Type e.g. DropDown or Checkbox*/
    private String uiType;

    private List<String> uiOptions; // Options for UI elements like DropDown or Checkbox




    @JsonIgnore
    private PatientReport patientReport;  // Report instance associated with the segment

    private int segmentOrder;

    public void generateUUID() {
        if (id == null || id.isEmpty()) {
            this.id = UUID.randomUUID().toString();  // Generate UUID if not present
        }
    }


    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public List<String> getUiOptions() {
        return uiOptions;
    }

    public void setUiOptions(List<String> uiOptions) {
        this.uiOptions = uiOptions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getReferenceSegment() {
        return referenceSegment;
    }

    public void setReferenceSegment(String referenceSegment) {
        this.referenceSegment = referenceSegment;
    }

    public PatientReport getPatientReport() {
        return patientReport;
    }

    public void setPatientReport(PatientReport patientReport) {
        this.patientReport = patientReport;
    }

    public int getSegmentOrder() {
        return segmentOrder;
    }

    public void setSegmentOrder(int segmentOrder) {
        this.segmentOrder = segmentOrder;
    }
}
