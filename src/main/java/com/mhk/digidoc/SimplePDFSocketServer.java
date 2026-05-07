package com.mhk.digidoc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mhk.digidoc.pdf.PDFReportGenerator;
import com.mhk.digidoc.entity.ReportWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplePDFSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(SimplePDFSocketServer.class);

    public static void main(String[] args) throws IOException {
        int port = 8089;
        int threadPoolSize = 10;
        
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        ServerSocket serverSocket = new ServerSocket(port);
        logger.info("Server listening on port {} with thread pool size {}", port, threadPoolSize);

        // Shutdown hook to gracefully close thread pool
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down server...");
            executorService.shutdown();
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.error("Error closing server socket", e);
            }
        }));

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                logger.info("Client connected: {}", clientSocket.getInetAddress());
                
                // Submit the client handling task to the thread pool
                executorService.submit(() -> handleClient(clientSocket));
            } catch (Exception e) {
                logger.error("Error accepting client connection", e);
            }
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket) {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            DataInputStream dataIn = new DataInputStream(in);
            DataOutputStream dataOut = new DataOutputStream(out);

            // Read operation code
            int operation = dataIn.readInt();
            logger.info("Received operation code: {}", operation);

            if (operation == 1) {
                // Operation 1: PDF generation (as before)
                int length = dataIn.readInt();
                byte[] base64Bytes = new byte[length];
                dataIn.readFully(base64Bytes);
                if (base64Bytes.length == 0) {
                    logger.warn("No data received from client");
                    out.write("No data received".getBytes());
                    return;
                }

                logger.debug("Decoding and parsing JSON data");
                // Decode and parse JSON
                byte[] jsonBytes = Base64.getDecoder().decode(base64Bytes);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.registerModule(new JavaTimeModule());
                ReportWrapper wrapper = objectMapper.readValue(jsonBytes, ReportWrapper.class);

                // Generate PDF
                ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
                PDFReportGenerator generator = new PDFReportGenerator();
                generator.loadHTML(pdfOut, wrapper.getReportTemplate(), wrapper.isInProgress());

                // Send PDF bytes length, then PDF bytes
                byte[] pdfBytes = pdfOut.toByteArray();
                dataOut.writeInt(pdfBytes.length);
                dataOut.write(pdfBytes);
                dataOut.flush();

                logger.info("PDF sent to client ({} bytes)", pdfBytes.length);
            } else if (operation == 2) {
                // Operation 2: Image resize (aspect-ratio preserving)
                // Protocol: [maxDimension:4][imgLength:4][imgBytes:N]
                int maxDimension = dataIn.readInt();
                int imgLength = dataIn.readInt();
                byte[] imgBytes = new byte[imgLength];
                dataIn.readFully(imgBytes);
                if (imgBytes.length == 0) {
                    logger.warn("No image data received from client");
                    dataOut.writeInt(0);
                    dataOut.flush();
                    return;
                }
                logger.debug("Resizing image ({} bytes) to maxDimension={}", imgBytes.length, maxDimension);
                ByteArrayInputStream imgIn = new ByteArrayInputStream(imgBytes);
                ByteArrayOutputStream thumbOut = new ByteArrayOutputStream();
                try {
                    // Thumbnailator maintains aspect ratio when using size() with keepAspectRatio
                    net.coobird.thumbnailator.Thumbnails.of(imgIn)
                        .size(maxDimension, maxDimension)
                        .keepAspectRatio(true)
                        .outputFormat("jpg")
                        .toOutputStream(thumbOut);
                } catch (Exception ex) {
                    logger.error("Image resize failed", ex);
                    dataOut.writeInt(0);
                    dataOut.flush();
                    return;
                }
                byte[] thumbBytes = thumbOut.toByteArray();
                dataOut.writeInt(thumbBytes.length);
                dataOut.write(thumbBytes);
                dataOut.flush();
                logger.info("Resized image sent to client ({} bytes, maxDimension={})", thumbBytes.length, maxDimension);
            } else {
                logger.warn("Unknown operation code: {}", operation);
                out.write("Unknown operation code".getBytes());
            }
        } catch (Exception e) {
            logger.error("Error processing client request", e);
        }
    }
}