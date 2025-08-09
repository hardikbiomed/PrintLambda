package com.mhk.digidoc.pdf;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.mhk.digidoc.entity.Patient;
import com.mhk.digidoc.entity.PatientReport;
import com.mhk.digidoc.entity.PatientReportSegment;
import com.mhk.digidoc.entity.ReportWrapper;
import com.mhk.digidoc.handler.PrintHandler;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PDFReportGenerator {


    public void loadHTML(ByteArrayOutputStream bos, Patient patient, PatientReport report, String tenantId, String htmlContent) {
            String templateName = report.getReportName().toLowerCase().replaceAll(" ","_")+".html";
            try{

                if (htmlContent == null || htmlContent.trim().isEmpty()) {
                    throw new IllegalStateException("HTML content is empty. Check S3 object and path.");
                }

                // Replace nulls with empty strings before replacing placeholders
                htmlContent = htmlContent.replace("{{patientname}}",
                        (patient.getFirstName() != null ? patient.getFirstName() : "") + " " +
                                (patient.getLastName() != null ? patient.getLastName() : ""));

                htmlContent = htmlContent.replace("{{patientgender}}",
                        patient.getGender() != null ? patient.getGender() : "");

                htmlContent = htmlContent.replace("{{address}}",
                        patient.getAddress() != null ? patient.getAddress() : "");

// htmlContent = htmlContent.replace("{{patientage}}", DateUtils.getAge(patient.getDateOfBirth()));

                htmlContent = htmlContent.replace("{{patientdob}}",
                        patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : "");

                htmlContent = htmlContent.replace("{{phonenumber}}",
                        patient.getContactNumber() != null ? patient.getContactNumber() : "");

                List<PatientReportSegment> segmentList = report.getSegments();
                for (PatientReportSegment segment: segmentList){
                    String segmentName= segment.getTitle().trim().toLowerCase().replaceAll(" ","");
                    htmlContent= htmlContent.replace("{{"+segmentName+"}}", segment.getWebContent());

                }
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.withHtmlContent(htmlContent, null);
                // Set the output PDF file
                builder.toStream(bos);
                // Build and render the PDF
                builder.run();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

}