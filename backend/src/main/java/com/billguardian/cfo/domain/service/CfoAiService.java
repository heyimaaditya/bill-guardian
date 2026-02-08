package com.billguardian.cfo.domain.service;

import com.billguardian.cfo.domain.model.Transaction;

public interface CfoAiService {
    /**
     * Analyzes a transaction and returns advice or a negotiation script.
     */
    String getAdvice(Transaction transaction);
}