package com.mhk.digidoc.pdf;


import com.mhk.digidoc.entity.Patient;
import com.mhk.digidoc.entity.PatientReport;
import com.mhk.digidoc.entity.PatientReportSegment;
import com.mhk.digidoc.util.DateUtils;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.awt.*;
import java.io.*;
import java.util.List;

public class PDFReportGenerator {


    public void loadHTML(ByteArrayOutputStream bos, Patient patient, PatientReport report, String htmlContent) {
        try {
            if (htmlContent == null || htmlContent.trim().isEmpty()) {
                throw new IllegalStateException("HTML content is empty. Check S3 object and path.");
            }

            // Replace placeholders
            htmlContent = htmlContent.replace("{{patientname}}",
                    (patient.getFirstName() != null ? patient.getFirstName() : "") + " " +
                            (patient.getLastName() != null ? patient.getLastName() : ""));

            htmlContent = htmlContent.replace("{{patientid}}",
                    patient.getPatientID() != null ? patient.getPatientID() : "");

            htmlContent = htmlContent.replace("{{dob}}",
                    patient.getDateOfBirth() != null ? DateUtils.getFormatedDate(patient.getDateOfBirth()) : "");

            htmlContent = htmlContent.replace("{{patientgender}}",
                    patient.getGender() != null ? patient.getGender() : "");

            htmlContent = htmlContent.replace("{{reportid}}",
                    report.getReportID() != null ? report.getReportID() : "");

            htmlContent = htmlContent.replace("{{address}}",
                    patient.getAddress() != null ? patient.getAddress() : "");


             htmlContent = htmlContent.replace("{{reportdate}}",
                        report.getReportEndDate() != null ? DateUtils.getFormatedDate(report.getReportEndDate()) : "");

            htmlContent = htmlContent.replace("{{patientage}}", DateUtils.getAge(patient.getDateOfBirth()));
            htmlContent = htmlContent.replace("{{patientdob}}",
                    patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : "");
            htmlContent = htmlContent.replace("{{phonenumber}}",
                    patient.getContactNumber() != null ? patient.getContactNumber() : "");

            List<PatientReportSegment> segmentList = report.getSegments();
            for (PatientReportSegment segment : segmentList) {
                String segmentName = segment.getTitle().trim().toLowerCase().replaceAll(" ", "");
                htmlContent = htmlContent.replace("{{" + segmentName + "}}", segment.getWebContent());
            }

            // Render PDF to a temporary stream
            ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.toStream(tempOut);
            builder.run();

            // Add watermark if needed
            if (report.getReportStatus() != null && !report.getReportStatus().equalsIgnoreCase("Complete")) {
                try (PDDocument document = PDDocument.load(new ByteArrayInputStream(tempOut.toByteArray()))) {
                    for (PDPage page : document.getPages()) {
                        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);

                        PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                        graphicsState.setNonStrokingAlphaConstant(0.2f);
                        contentStream.setGraphicsStateParameters(graphicsState);

                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 80);
                        contentStream.setNonStrokingColor(Color.GRAY);

                        float pageWidth = page.getMediaBox().getWidth();
                        float pageHeight = page.getMediaBox().getHeight();
                        String watermark = "In Progress";
                        float fontSize = 80f;
                        float stringWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(watermark) / 1000 * fontSize;

                        // Center coordinates
                        float centerX = pageWidth / 2;
                        float centerY = pageHeight / 2;

                        // Offset so the text is centered
                        float tx = centerX - (stringWidth / 2);
                        float ty = centerY;

                        contentStream.beginText();
                        contentStream.setTextMatrix(
                                org.apache.pdfbox.util.Matrix.getRotateInstance(Math.toRadians(45), tx, ty)
                        );
                        contentStream.showText(watermark);
                        contentStream.endText();

                        contentStream.close();
                    }
                    document.save(bos);
                }
            } else {
                bos.write(tempOut.toByteArray());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}