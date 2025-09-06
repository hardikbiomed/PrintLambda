package com.mhk.digidoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mhk.digidoc.entity.Patient;
import com.mhk.digidoc.entity.PatientReport;
import com.mhk.digidoc.entity.PatientReportSegment;
import com.mhk.digidoc.entity.ReportTemplate;
import com.mhk.digidoc.pdf.PDFReportGenerator;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class LocalMain {

    public static void main(String[] args) throws Exception {
        // This is a placeholder for local testing.
        // You can run your tests here or invoke methods from your classes to see how they behave.
        System.out.println("LocalMain is running. You can add your test logic here.");

        // Example of creating a new instance of ReportWrapper and printing its class name
        // ReportWrapper reportWrapper = new ReportWrapper();
        // System.out.println("Created instance of: " + reportWrapper.getClass().getName());
        String zipPath = args[0];
        boolean jsonFound = false, htmlFound = false;
        String htmlFileName = null;
        byte[] htmlContent = null;
        ReportTemplate template =  null;
        InputStream inputStream = new FileInputStream(zipPath);
        byte[]  zipContent = inputStream.readAllBytes();
        inputStream.close();
        // Use Quarkus FileUpload API
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".json")) {
                    jsonFound = true;
                    String json = new String(zis.readAllBytes());
                    try{
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.registerModule(new JavaTimeModule());
                        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                        template = mapper.readValue(json, ReportTemplate.class);
                        System.out.println("Parsed JSON successfully: " + template.getName());
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                } else if (entry.getName().endsWith(".html")) {
                    htmlFound = true;
                    htmlFileName = entry.getName();
                    htmlContent = zis.readAllBytes();
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing uploaded zip file: " + e.getMessage());
            return;
        }
        if (!jsonFound || !htmlFound) {
            System.out.println("Missing JSON or HTML file ini ZIP");
            return;
        }
        if(template == null){
            System.out.println("Template is null");
            return;
        }
        FileInputStream fis = new FileInputStream("patient.json");
        byte[] patientBytes = fis.readAllBytes();
        fis.close();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Patient patient = mapper.readValue(patientBytes, Patient.class);
        PatientReport report = new PatientReport();
        report.setPatient(patient);
        report.setSegments(new ArrayList<>());
        template.getReportTemplateSegments().forEach(segment->{
            PatientReportSegment reportSegment = new PatientReportSegment();
            try {
                System.out.println("Enter content for segment " + segment.getTitle() + ": ");
                reportSegment.setWebContent(readInputfromKeyBoard());
                reportSegment.setTitle(segment.getTitle());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            report.getSegments().add(reportSegment);
        });
        PDFReportGenerator pdfReportGenerator = new PDFReportGenerator();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        pdfReportGenerator.loadHTML(bos, patient, report, new String(htmlContent));
        byte[] pdfBytes = bos.toByteArray();
        //save PDF
        FileOutputStream fos = new FileOutputStream("output.pdf");
        fos.write(pdfBytes);
        fos.close();
    }

    static String readInputfromKeyBoard() throws IOException {
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

}
