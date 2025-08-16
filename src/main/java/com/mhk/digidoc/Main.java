package com.mhk.digidoc;

import com.mhk.digidoc.entity.Patient;
import com.mhk.digidoc.entity.PatientReport;
import com.mhk.digidoc.entity.PatientReportSegment;
import com.mhk.digidoc.pdf.PDFReportGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static Patient createSamplePatient() {
        Patient patient = new Patient();
        patient.setInternalUUID(UUID.randomUUID().toString());
        patient.setPatientID("P123456");
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(new Date(90, 0, 1)); // 1990-01-01
        patient.setContactNumber("+1234567890");
        patient.setEmailID("john.doe@example.com");
        patient.setGender("Male");
        patient.setAddress("123 Main St, City, Country");
        patient.setMaritalStatus("Single");
        patient.setNationality("Countryland");
        patient.setCreationTime(java.time.LocalDateTime.now());
        patient.setLastAccessedTime(java.time.LocalDateTime.now());
        patient.setVerificationStatus("Verified");

        // Create a sample report
        PatientReport report = new PatientReport();
        report.setReportInstanceUUID(UUID.randomUUID().toString());
        report.setReportName("Sample Report");
        report.setReportStartDate(new Date());
        report.setReportStatus("Draft");
        report.setAssignedTo("Dr. Smith");
        report.setSignedBy("Dr. Smith");
        report.setReferredBy("Dr. Jones");
        report.setLastUpdatedTime("2023-10-01T12:00:00");
        report.setRemarks("Initial report");
        report.setTemplateRererenceID("template-123");
        report.setReportId("report-123");
        report.setDepartment("Cardiology");
        report.setPatient(patient);

        java.util.List<PatientReport> reports = new java.util.ArrayList<>();
        reports.add(report);
        patient.setReports(reports);



        List<PatientReportSegment> segments = new ArrayList<>();

        PatientReportSegment seg1 = new PatientReportSegment();
        seg1.setTitle("Observation History");
        seg1.setWebContent(
                "The patient presented with a history of mild fever and persistent fatigue over the past week. " +
                        "No significant weight loss or night sweats were reported. " +
                        "Physical examination revealed a slightly elevated temperature and mild pallor. " +
                        "There were no signs of acute distress or respiratory complications. " +
                        "The patient denied any recent travel or exposure to infectious diseases."
        );
        seg1.setSegmentOrder(1);
        seg1.setPatientReport(report);

        PatientReportSegment seg2 = new PatientReportSegment();
        seg2.setTitle("Medication");
        seg2.setWebContent(
                "The patient was prescribed Paracetamol 500mg to be taken twice daily for five days. " +
                        "In addition, a multivitamin supplement was recommended to support overall health. " +
                        "The patient was advised to avoid non-steroidal anti-inflammatory drugs due to a history of mild gastritis. " +
                        "No antibiotics were deemed necessary at this stage. " +
                        "The patient was instructed to report any adverse reactions immediately."
        );
        seg2.setSegmentOrder(2);
        seg2.setPatientReport(report);

        PatientReportSegment seg3 = new PatientReportSegment();
        seg3.setTitle("Recommendation");
        seg3.setWebContent(
                "It is recommended that the patient increases fluid intake to stay well hydrated. " +
                        "Adequate rest and a balanced diet are essential for recovery. " +
                        "The patient should monitor body temperature twice daily and keep a record of any new symptoms. " +
                        "Avoid strenuous activities until full recovery is achieved. " +
                        "If symptoms worsen or new issues arise, the patient should seek medical attention promptly."
        );
        seg3.setSegmentOrder(3);
        seg3.setPatientReport(report);

        PatientReportSegment seg4 = new PatientReportSegment();
        seg4.setTitle("Treatment Plan");
        seg4.setWebContent(
                "The treatment plan involves monitoring the patient's temperature and symptoms for one week. " +
                        "Follow the prescribed medication regimen and maintain a symptom diary. " +
                        "A follow-up blood test may be considered if symptoms persist beyond the initial period. " +
                        "The patient should avoid contact with individuals showing signs of infection. " +
                        "Regular updates via teleconsultation are encouraged to track progress."
        );
        seg4.setSegmentOrder(4);
        seg4.setPatientReport(report);

        PatientReportSegment seg5 = new PatientReportSegment();
        seg5.setTitle("Follow Up");
        seg5.setWebContent(
                "A follow-up visit is scheduled after seven days to assess the patient's progress. " +
                        "During the visit, a repeat physical examination and review of the symptom diary will be conducted. " +
                        "If necessary, additional laboratory investigations will be ordered. " +
                        "The patient is encouraged to communicate any concerns before the scheduled appointment. " +
                        "Continued monitoring and support will be provided as needed."
        );
        seg5.setSegmentOrder(5);
        seg5.setPatientReport(report);

        segments.add(seg1);
        segments.add(seg2);
        segments.add(seg3);
        segments.add(seg4);
        segments.add(seg5);

        report.setSegments(segments);


        return patient;
    }

    public static void main(String[] args) throws Exception {
       //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        PDFReportGenerator generator = new PDFReportGenerator();
        Patient patient = createSamplePatient();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileInputStream fileInputStream = new FileInputStream(new File(("/home/oem/software/digidoc/reports_samples/dental_report/treatment_summary.html")));
        String fileData = new String(fileInputStream.readAllBytes());
        generator.loadHTML(byteArrayOutputStream, patient, patient.getReports().get(0), "tenant-123", fileData);
        fileInputStream.close();
        FileOutputStream fos = new FileOutputStream(new File("/home/oem/software/digidoc/reports_samples/dental_report/treatment_summary.pdf"));
        byteArrayOutputStream.writeTo(fos);
        fos.close();
        System.out.println("PDF generated successfully.");
    }
}