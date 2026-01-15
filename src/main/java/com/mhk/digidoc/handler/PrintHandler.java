package com.mhk.digidoc.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mhk.digidoc.entity.ReportWrapper;
import com.mhk.digidoc.pdf.PDFReportGenerator;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintHandler implements RequestHandler<ReportWrapper, String> {

    private static final Logger logger = LoggerFactory.getLogger(PrintHandler.class);

    @Override
    public String handleRequest(ReportWrapper wrapper, Context context) {
        logger.info("Starting PDF generation for patient: {}", wrapper.getPatient() != null ? wrapper.getPatient().getPatientID() : "unknown");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PDFReportGenerator reportGenerator = new PDFReportGenerator();
            reportGenerator.loadHTML(byteArrayOutputStream, wrapper.getPatient(), wrapper.getPatientReport(), wrapper.getReportContent());
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            String response = Base64.getEncoder().encodeToString(pdfBytes);
            logger.info("PDF generated successfully, size: {} bytes", pdfBytes.length);
            return response;
        } catch (Exception e) {
            logger.error("Error generating PDF", e);
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

}
