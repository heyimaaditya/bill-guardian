package com.billguardian.cfo.dto;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class AnalyticsSummary implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private BigDecimal totalMonthlyBurn;
    private BigDecimal totalPotentialSavings;
    private Map<String, BigDecimal> spendingByCategory;
}