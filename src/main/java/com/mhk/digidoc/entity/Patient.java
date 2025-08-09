package com.mhk.digidoc.entity;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Patient  {

    private String internalUUID;  // Unique identifier for the patient

    private String patientID; // Patient’s identifier

    private String firstName; // First name of the patient

    private String lastName;  // Last name of the patient

    private Date dateOfBirth; // Date of birth of the patient

    private String contactNumber;  // Contact number of the patient

    private String emailID;  // Email ID of the patient

    private String gender;  // Gender of the patient

    private String address;  // Address of the patient

    private String maritalStatus;  // Marital status of the patient

    private String nationality;  // Nationality of the patient

    private LocalDateTime creationTime;  // Creation time of the patient’s record

    private LocalDateTime lastAccessedTime;

    private String verificationStatus;  // Verification status for the patient’s contact details

    private List<PatientReport> reports;  // List of reports associated with the patient

    public void generateUUID() {
        if (internalUUID == null) {
            this.internalUUID = UUID.randomUUID().toString();  // Generate UUID if not present
        }
    }
    public LocalDateTime getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(LocalDateTime lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public String getInternalUUID() {
        return internalUUID;
    }

    public void setInternalUUID(String internalUUID) {
        this.internalUUID = internalUUID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public List<PatientReport> getReports() {
        return reports;
    }

    public void setReports(List<PatientReport> reports) {
        this.reports = reports;
    }
}