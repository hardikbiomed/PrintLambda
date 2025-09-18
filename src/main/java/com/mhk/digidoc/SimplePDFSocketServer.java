package com.mhk.digidoc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mhk.digidoc.pdf.PDFReportGenerator;
import com.mhk.digidoc.entity.ReportWrapper;

public class SimplePDFSocketServer {
    public static void main(String[] args) throws IOException {
        int port = 8089;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                InputStream in = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream();

                // Read all bytes until client closes connection
                DataInputStream dataIn = new DataInputStream(in);
                int length = dataIn.readInt();
                byte[] base64Bytes = new byte[length];
                dataIn.readFully(base64Bytes);
                if (base64Bytes.length == 0) {
                    out.write("No data received".getBytes());
                    continue;
                }

                // Decode and parse JSON
                byte[] jsonBytes = Base64.getDecoder().decode(base64Bytes);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                objectMapper.registerModule(new JavaTimeModule());
                ReportWrapper wrapper = objectMapper.readValue(jsonBytes, ReportWrapper.class);

                // Generate PDF
                ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
                PDFReportGenerator generator = new PDFReportGenerator();
                generator.loadHTML(pdfOut, wrapper.getPatient(), wrapper.getPatientReport(), wrapper.getReportContent());

                // Send PDF bytes length, then PDF bytes
                byte[] pdfBytes = pdfOut.toByteArray();
                DataOutputStream dataOut = new DataOutputStream(out);
                dataOut.writeInt(pdfBytes.length);
                dataOut.write(pdfBytes);
                dataOut.flush();

                System.out.println("PDF sent to client (" + pdfBytes.length + " bytes)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}