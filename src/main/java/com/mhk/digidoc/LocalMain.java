package com.mhk.digidoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mhk.digidoc.entity.Patient;
import com.mhk.digidoc.entity.PatientReport;
import com.mhk.digidoc.entity.PatientReportSegment;
import com.mhk.digidoc.entity.ReportTemplate;
import com.mhk.digidoc.pdf.PDFReportGenerator;

import java.io.*;
import java.util.ArrayList;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalMain {

    private static final Logger logger = LoggerFactory.getLogger(LocalMain.class);

    public static void main(String[] args) throws Exception {
        logger.info("Starting LocalMain for PDF generation test");
        // This is a placeholder for local testing.
        // You can run your tests here or invoke methods from your classes to see how they behave.
        System.out.println("LocalMain is running. You can add your test logic here.");

        File parentDirectory = new File(args[0]);
        if(!parentDirectory.exists()){
            logger.error("Directory not found: {}", args[0]);
            System.out.println("Directory Not found "+ args[0]);
            return;
        }
        File jsonFile = null;
        File htmlFIle = null;

        File[] allFiles = parentDirectory.listFiles();
        assert allFiles !=null;
        for(File file: allFiles){
            if(file.getName().endsWith(".json")){
                jsonFile = file;
            }else if(file.getName().endsWith(".html")) {
                htmlFIle = file;
            }
        }
        if(jsonFile == null || htmlFIle == null){
            logger.error("JSON or HTML file not found in directory: {}", args[0]);
            System.out.println("Exiting ... JSON or HTML file not found in directory "+ args[0]);
            return;
        }

        logger.debug("Reading HTML and JSON content");
        String htmlContent = getData(htmlFIle);
        String jsonContent = getData(jsonFile);

        ReportTemplate template =  null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        template = mapper.readValue(jsonContent, ReportTemplate.class);
        logger.info("Parsed JSON successfully: {}", template.getName());
        System.out.println("Parsed JSON successfully: " + template.getName());

        String expectedHTMLFileName = template.getName().trim().toLowerCase().replaceAll(" ", "_").toLowerCase()+".html";
        if(!expectedHTMLFileName.equals(htmlFIle.getName())){
            logger.error("HTML file name does not match template name. Expected: {} Found: {}", expectedHTMLFileName, htmlFIle.getName());
            System.out.println("Exiting HTML file name does not match template name. Expected: "+expectedHTMLFileName+" Found: "+htmlFIle.getName());
            return;
        }
        FileInputStream fis = new FileInputStream("patient.json");
        byte[] patientBytes = fis.readAllBytes();
        fis.close();

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
                logger.error("Error reading input for segment: {}", segment.getTitle(), e);
                throw new RuntimeException(e);
            }
            report.getSegments().add(reportSegment);
        });
        logger.info("Generating PDF for patient: {}", patient.getPatientID());
        PDFReportGenerator pdfReportGenerator = new PDFReportGenerator();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        pdfReportGenerator.loadHTML(bos, patient, report, htmlContent);
        byte[] pdfBytes = bos.toByteArray();
        //save PDF
        FileOutputStream fos = new FileOutputStream(parentDirectory+"/output.pdf");
        fos.write(pdfBytes);
        fos.close();
        logger.info("PDF saved successfully to: {}", parentDirectory+"/output.pdf");
    }

    static String readInputfromKeyBoard() throws IOException {
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    static String getData(File file) throws Exception{
        FileInputStream fis = new FileInputStream(file);
        byte[] allByte = fis.readAllBytes();
        fis.close();
        return new String(allByte);
    }


}
