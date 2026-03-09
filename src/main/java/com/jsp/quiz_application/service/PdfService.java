package com.jsp.quiz_application.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.jsp.quiz_application.entity.Payment;
import com.jsp.quiz_application.entity.User;
import com.jsp.quiz_application.entity.UserResult;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    // ===============================
    // QUIZ RESULT PDF
    // ===============================

    public byte[] generateResultPdf(User user, UserResult result) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font textFont = new Font(Font.FontFamily.HELVETICA, 12);

        Paragraph title = new Paragraph("QUIZ RESULT REPORT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // User Info Table
        PdfPTable userTable = new PdfPTable(2);
        userTable.setWidthPercentage(100);

        userTable.addCell(createHeaderCell("User Name"));
        userTable.addCell(createValueCell(user.getUserName()));

        userTable.addCell(createHeaderCell("Quiz ID"));
        userTable.addCell(createValueCell(String.valueOf(result.getQuiz().getId())));

        document.add(userTable);

        document.add(new Paragraph(" "));

        // Result Table
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(createHeaderCell("Total Attempted"));
        table.addCell(createValueCell(String.valueOf(result.getTotalAttempted())));

        table.addCell(createHeaderCell("Correct Answers"));
        table.addCell(createValueCell(String.valueOf(result.getCorrectAnswer())));

        table.addCell(createHeaderCell("Wrong Answers"));
        table.addCell(createValueCell(String.valueOf(result.getWrongAnswer())));

        document.add(table);

        document.close();

        return outputStream.toByteArray();
    }


    // ===============================
    // PAYMENT RECEIPT PDF
    // ===============================

    public byte[] generatePaymentReceipt(Payment payment) throws Exception {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);

        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);

        Paragraph title = new Paragraph("PAYMENT RECEIPT", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);

        document.add(title);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(createHeaderCell("Order ID"));
        table.addCell(createValueCell(payment.getOrderId()));

        table.addCell(createHeaderCell("Payment ID"));
        table.addCell(createValueCell(payment.getPaymentId()));

        table.addCell(createHeaderCell("Amount"));
        table.addCell(createValueCell("₹ " + payment.getAmount()));

        table.addCell(createHeaderCell("Status"));
        table.addCell(createValueCell(payment.getStatus()));

        table.addCell(createHeaderCell("User"));
        table.addCell(createValueCell(payment.getUser().getUserName()));

        table.addCell(createHeaderCell("Quiz"));
        table.addCell(createValueCell(payment.getQuiz().getTitle()));

        document.add(table);

        document.close();

        return outputStream.toByteArray();
    }


    // ===============================
    // TABLE DESIGN HELPERS
    // ===============================

    private PdfPCell createHeaderCell(String text) {

        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(10);

        return cell;
    }

    private PdfPCell createValueCell(String text) {

        Font font = new Font(Font.FontFamily.HELVETICA, 12);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(10);

        return cell;
    }
}