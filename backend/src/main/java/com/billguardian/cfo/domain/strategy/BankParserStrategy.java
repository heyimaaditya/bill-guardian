package com.billguardian.cfo.domain.strategy;

import com.billguardian.cfo.domain.model.Transaction;
import java.util.List;

public interface BankParserStrategy {
    List<Transaction> parse(byte[] fileContent);
    
    String getBankName();
    boolean supports(String firstLine);
}