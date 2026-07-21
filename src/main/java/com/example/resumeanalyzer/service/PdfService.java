package com.example.resumeanalyzer.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;



@Service
public class PdfService {



    public byte[] generateReport(String analysis)
            throws Exception {



        Document document = new Document();



        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();



        PdfWriter.getInstance(
                document,
                outputStream
        );



        document.open();



        Font titleFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        18
                );



        document.add(
                new Paragraph(
                        "AI Resume Analyzer Report",
                        titleFont
                )
        );



        document.add(
                new Paragraph("\n")
        );



        document.add(
                new Paragraph(
                        analysis
                )
        );



        document.close();



        return outputStream.toByteArray();


    }


}