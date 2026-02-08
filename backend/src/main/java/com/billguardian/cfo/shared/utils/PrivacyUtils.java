package com.billguardian.cfo.shared.utils;

import java.util.regex.Pattern;

public class PrivacyUtils {

    // Regex for: Credit Card numbers, Bank Account numbers (usually 10-12 digits)
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("\\b\\d{10,16}\\b");
    
    // Regex for: Email addresses
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    public static String maskSensitiveData(String input) {
        if (input == null) return null;
        
        String masked = ACCOUNT_PATTERN.matcher(input).replaceAll("****-****-****");
        masked = EMAIL_PATTERN.matcher(masked).replaceAll("[EMAIL_HIDDEN]");
        
        return masked;
    }
}