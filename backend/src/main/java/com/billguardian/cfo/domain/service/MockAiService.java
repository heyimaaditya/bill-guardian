package com.billguardian.cfo.domain.service;

import com.billguardian.cfo.domain.model.Transaction;
import org.springframework.stereotype.Service;

@Service
public class MockAiService implements CfoAiService {

    @Override
    public String getAdvice(Transaction transaction) {
        String merchant = transaction.getMerchantName().toLowerCase();

        // Simulate AI "Thinking" based on categories
        if (merchant.contains("electricity") || merchant.contains("gas")) {
            return "AI ADVICE: Your utility bill is 15% higher than your neighbors. " +
                   "SCRIPT: 'I've been a loyal customer for 12 months. I see [Competitor] " +
                   "is offering a lower rate. Can you match it?'";
        }

        if (merchant.contains("netflix") || merchant.contains("spotify")) {
            return "AI ADVICE: Possible Zombie Subscription. Have you used this in the last 30 days? " +
                   "If not, cancel via their settings page to save $" + transaction.getAmount();
        }

        return "AI ADVICE: This looks like a standard lifestyle expense. No action needed.";
    }
}