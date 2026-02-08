package com.billguardian.cfo.domain.strategy;

import com.billguardian.cfo.domain.model.Transaction;
import com.billguardian.cfo.domain.model.TransactionCategory;
import com.billguardian.cfo.shared.utils.HashUtils;    // <--- ADD THIS IMPORT
import com.billguardian.cfo.shared.utils.PrivacyUtils; // <--- ADD THIS IMPORT
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ChaseParser implements BankParserStrategy {

    @Override
    public List<Transaction> parse(byte[] fileContent) {
        List<Transaction> transactions = new ArrayList<>();
        String[] lines = new String(fileContent).split("\n");

        for (int i = 1; i < lines.length; i++) {
            String originalLine = lines[i].trim();
            if (originalLine.isEmpty()) continue;

            try {
                String[] columns = originalLine.split(",");
                if (columns.length < 3) continue;

                // Create a unique fingerprint based on the raw data
                String fingerprint = HashUtils.generateFingerprint(originalLine);
                String maskedData = PrivacyUtils.maskSensitiveData(originalLine);

                transactions.add(Transaction.builder()
                        .transactionDate(LocalDate.parse(columns[0].trim()))
                        .amount(new BigDecimal(columns[1].trim()))
                        .description(columns[2].trim())
                        .merchantName(columns[2].trim())
                        .category(TransactionCategory.UNCATEGORIZED)
                        .originalBankName(getBankName())
                        .rawData(maskedData)
                        .hashIdentifier(fingerprint) // Set the unique ID
                        .build());

            } catch (Exception e) {
                log.error("Error parsing line {}: {}", i, e.getMessage());
            }
        }
        return transactions;
    }

    @Override
    public String getBankName() { return "CHASE"; }

    @Override
    public boolean supports(String firstLine) {
        return firstLine.contains("Date") && firstLine.contains("Description");
    }
}