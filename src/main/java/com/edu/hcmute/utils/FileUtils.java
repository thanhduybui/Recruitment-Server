package com.edu.hcmute.utils;


import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileUtils {
  public static MultipartFile docxToPdf(MultipartFile file) throws IOException {

    try (InputStream docxInputStream = file.getInputStream()) {
      XWPFDocument doc = new XWPFDocument(docxInputStream);

      // Create PDF options
      PdfOptions pdfOptions = PdfOptions.create();

      // Create output stream for PDF file
      ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

      // Convert DOCX to PDF
      PdfConverter.getInstance().convert(doc, pdfOutputStream, pdfOptions);

      // Generate PDF file name
      String pdfFileName = file.getOriginalFilename().replace(".docx", ".pdf");

      // Create MultipartFile from PDF output stream
      byte[] pdfBytes = pdfOutputStream.toByteArray();
       return new MultipartFile() {
         @Override
         public String getName() {
           return file.getName();
         }

         @Override
         public String getOriginalFilename() {
           return pdfFileName;
         }

         @Override
         public String getContentType() {
           return "application/pdf";
         }

         @Override
         public boolean isEmpty() {
           return pdfBytes.length == 0;
         }

         @Override
         public long getSize() {
           return pdfBytes.length;
         }

         @Override
         public byte[] getBytes() throws IOException {
           return pdfBytes;
         }

         @Override
         public InputStream getInputStream() throws IOException {
           return new ByteArrayInputStream(pdfBytes);
         }

         @Override
         public void transferTo(File dest) throws IOException, IllegalStateException {
           new FileOutputStream(dest).write(pdfBytes);
         }
       };

    } catch (IOException e) {
      // Handle IOException
      throw e;
    }

  }
}
