package com.billguardian.cfo.domain.strategy;

import com.billguardian.cfo.domain.model.Transaction;
import com.billguardian.cfo.domain.model.TransactionCategory;
import com.billguardian.cfo.shared.utils.PrivacyUtils;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AmexParser implements BankParserStrategy {

    @Override
    public List<Transaction> parse(byte[] fileContent) {
        List<Transaction> transactions = new ArrayList<>();
        String[] lines = new String(fileContent).split("\n");

        for (int i = 1; i < lines.length; i++) {
            try {
                String[] columns = lines[i].split(",");
                if (columns.length < 3) continue;

                transactions.add(Transaction.builder()
                        .transactionDate(LocalDate.parse(columns[1].trim()))
                        .amount(new BigDecimal(columns[2].trim()))
                        .description(columns[0].trim())
                        .merchantName(columns[0].trim())
                        .category(TransactionCategory.UNCATEGORIZED)
                        .originalBankName(getBankName())
                        .rawData(PrivacyUtils.maskSensitiveData(lines[i]))
                        .build());
            } catch (Exception e) {}
        }
        return transactions;
    }

    @Override
    public String getBankName() { return "AMEX"; }

    @Override
    public boolean supports(String firstLine) {
        return firstLine.contains("Reference") || (firstLine.contains("Description") && firstLine.indexOf("Description") == 0);
    }
}