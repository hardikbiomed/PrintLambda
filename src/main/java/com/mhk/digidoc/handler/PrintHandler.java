package com.mhk.digidoc.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.mhk.digidoc.entity.ReportWrapper;
import com.mhk.digidoc.pdf.PDFReportGenerator;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class PrintHandler implements RequestHandler<ReportWrapper, String> {

    @Override
    public String handleRequest(ReportWrapper wrapper, Context context) {
        // Log input
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PDFReportGenerator reportGenerator = new PDFReportGenerator();
        reportGenerator.loadHTML(byteArrayOutputStream, wrapper.getPatient(), wrapper.getPatientReport(), wrapper.getReportContent());
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(pdfBytes);
    }

}
