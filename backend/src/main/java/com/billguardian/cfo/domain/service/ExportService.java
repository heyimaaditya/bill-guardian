package com.billguardian.cfo.domain.service;

import com.billguardian.cfo.domain.model.Transaction;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class ExportService {

    public byte[] generateNegotiationPdf(Transaction tx) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();
        
        // Header
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph header = new Paragraph("BillGuardian: Negotiation Evidence", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        document.add(new Paragraph("Generated on: Feb 08, 2026"));
        document.add(Chunk.NEWLINE);

        // Transaction Details
        document.add(new Paragraph("Merchant: " + tx.getMerchantName()));
        document.add(new Paragraph("Current Bill: $" + tx.getAmount()));
        document.add(new Paragraph("Market Fair Price: $" + tx.getCommunityAverage()));
        document.add(Chunk.NEWLINE);

        // The AI Evidence
        Font subhead = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        document.add(new Paragraph("Analysis:", subhead));
        document.add(new Paragraph("Our audit indicates you are being overcharged compared to the local average."));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Negotiation Script:", subhead));
        document.add(new Paragraph(tx.getAiAdvice()));

        document.close();
        return out.toByteArray();
    }
}