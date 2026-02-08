package com.billguardian.cfo.api;

import com.billguardian.cfo.domain.model.Transaction;
import com.billguardian.cfo.domain.repository.TransactionRepository;
import com.billguardian.cfo.domain.service.KafkaProducerService;
import com.billguardian.cfo.domain.service.AnalyticsService; 
import com.billguardian.cfo.dto.AnalyticsSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.billguardian.cfo.domain.service.ExportService;

@RestController
@RequestMapping("/api/statements")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class StatementController {

    private final KafkaProducerService kafkaProducerService;
    private final TransactionRepository transactionRepository;
    private final AnalyticsService analyticsService; 
    private final ExportService exportService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadStatement(@RequestParam("file") MultipartFile file) {
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            kafkaProducerService.sendStatementEvent(file.getOriginalFilename(), content);
            return ResponseEntity.accepted().body("File uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to read file.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

  
    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummary> getSummary() {
        return ResponseEntity.ok(analyticsService.getSummary());
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportEvidence(@PathVariable Long id) {
        Transaction tx = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        byte[] pdf = exportService.generateNegotiationPdf(tx);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=negotiation_evidence.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
