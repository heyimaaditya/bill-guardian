package com.billguardian.cfo.domain.strategy;

import com.billguardian.cfo.domain.model.Transaction;
import java.util.List;

public interface BankParserStrategy {
    // This defines what every bank parser MUST do
    List<Transaction> parse(byte[] fileContent);
    
    // This tells us which bank this parser handles
    String getBankName();
    boolean supports(String firstLine);
}