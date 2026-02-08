package com.billguardian.cfo.dto;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable; // <--- ADD THIS
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class AnalyticsSummary implements Serializable { // <--- ADD THIS
    private static final long serialVersionUID = 1L; // Senior Practice: versioning for serialization
    
    private BigDecimal totalMonthlyBurn;
    private BigDecimal totalPotentialSavings;
    private Map<String, BigDecimal> spendingByCategory;
}