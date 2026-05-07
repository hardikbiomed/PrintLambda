package com.mhk.digidoc.pdf;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.awt.*;
import java.io.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFReportGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PDFReportGenerator.class);

    static {
        // Disable PDFBox font cache globally to prevent Lambda timeout
        System.setProperty("pdfbox.fontcache", "");
        System.setProperty("java.awt.headless", "true");
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }

    public void loadHTML(ByteArrayOutputStream bos,  String htmlContent, boolean isInProgress) {

        try {
            if (htmlContent == null || htmlContent.trim().isEmpty()) {
                throw new IllegalStateException("HTML content is empty. Check S3 object and path.");
            }


            logger.debug("Rendering PDF from HTML as size {} bytes", htmlContent.length());

            ByteArrayOutputStream tempOut = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();

            // Disable font subset embedding to speed up processing
            builder.useColorProfile(null);

            builder.withHtmlContent(htmlContent, null);
            builder.toStream(tempOut);

            logger.info("About to call builder.run() - ensure Lambda has 512MB+ memory and 30s+ timeout");
            long startTime = System.currentTimeMillis();

            try {
                builder.run();
                logger.info("builder.run() completed in {} ms", System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                logger.error("builder.run() FAILED after {} ms: {}", System.currentTimeMillis() - startTime, e.getMessage(), e);
                throw new RuntimeException("PDF generation failed", e);
            }

            logger.info("PDF generated, size: {} bytes", tempOut.size());
            // Add watermark if needed
            if (isInProgress) {
                logger.info("Adding watermark to PDF as report status is: {}", isInProgress);
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
            logger.info("PDF generation completed successfully");
        } catch (Exception ex) {
            logger.error("Error during PDF generation", ex);

        }
    }

}