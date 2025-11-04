package com.kyron.filter.PiiDetector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// RegexPIIDetector.java
public class RegexPIIDetector {

    private static final Map<String, Pattern> PATTERNS = new LinkedHashMap<>();

    static {
        // Social Security Number (US)
        PATTERNS.put("SSN", Pattern.compile(
                "\\b\\d{3}-\\d{2}-\\d{4}\\b|\\b\\d{9}\\b"
        ));

        // Email Address
        PATTERNS.put("EMAIL", Pattern.compile(
                "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"
        ));

        // Phone Number (US formats)
        PATTERNS.put("PHONE", Pattern.compile(
                "\\b(?:\\+?1[-.\\s]?)?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}\\b"
        ));

        // Credit Card (with Luhn validation)
        PATTERNS.put("CREDIT_CARD", Pattern.compile(
                "\\b\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}[\\s-]?\\d{4}\\b|" +  // 16 digits: 4-4-4-4
                        "\\b\\d{4}[\\s-]?\\d{6}[\\s-]?\\d{5}\\b|" +       // 15 digits: 4-6-5 (Amex)
                        "\\b\\d{13,19}\\b"                                // 13-19 consecutive digits
        ));

        // IP Address (IPv4)
        PATTERNS.put("IP_ADDRESS", Pattern.compile(
                "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b"
        ));

        // US Zip Code
        PATTERNS.put("ZIP_CODE", Pattern.compile(
                "\\b\\d{5}(?:-\\d{4})?\\b"
        ));

        // Date detection (MM/DD/YYYY or YYYY/MM/DD)
        PATTERNS.put("CALENDAR_DATE", Pattern.compile(
                "\\b(?:0?[1-9]|1[0-2])[/-](?:0?[1-9]|[12][0-9]|3[01])[/-](?:19|20)\\d{2}\\b|" +
                        "\\b(?:19|20)\\d{2}[/-](?:0?[1-9]|1[0-2])[/-](?:0?[1-9]|[12][0-9]|3[01])\\b"
        ));

        // US Passport Number
        PATTERNS.put("PASSPORT", Pattern.compile(
                "\\b[A-Z]{1,2}\\d{6,9}\\b"
        ));

        // Driver's License (basic - varies by state)
        PATTERNS.put("DRIVERS_LICENSE", Pattern.compile(
                "\\b[A-Z]\\d{7,8}\\b|\\b\\d{9}\\b"
        ));

        // Bank Account Number (8-17 digits)
        PATTERNS.put("BANK_ACCOUNT", Pattern.compile(
                "\\b\\d{8,17}\\b"
        ));

        // URL
        PATTERNS.put("URL", Pattern.compile(
                "\\bhttps?://[A-Za-z0-9.-]+(?:/[A-Za-z0-9._~:/?#\\[\\]@!$&'()*+,;=-]*)?\\b"
        ));
    }

    public List<PIIEntity> detect(String text) {
        List<PIIEntity> entities = new ArrayList<>();

        for (Map.Entry<String, Pattern> entry : PATTERNS.entrySet()) {
            String type = entry.getKey();
            Matcher matcher = entry.getValue().matcher(text);

            while (matcher.find()) {
                String value = matcher.group();

                // Additional validation
                if (shouldInclude(type, value)) {
                    entities.add(new PIIEntity(
                            type,
                            value,
                            matcher.start(),
                            matcher.end(),
                            0.95  // High confidence for regex matches
                    ));
                }
            }
        }

        return entities;
    }

    private boolean shouldInclude(String type, String value) {
        switch (type) {
            case "CREDIT_CARD":
                return isValidCreditCard(value);
            case "SSN":
                return isValidSSN(value);
            case "ZIP_CODE":
                // Avoid false positives with other 5-digit numbers
                return true; // Add context-based filtering if needed
            case "BANK_ACCOUNT":
                // Too many false positives - consider removing or adding context
                return false; // Disabled by default
            default:
                return true;
        }
    }

    private boolean isValidCreditCard(String number) {
        String cleaned = number.replaceAll("[^0-9]", "");

        if (cleaned.length() < 13 || cleaned.length() > 19) {
            return false;
        }

        // Luhn algorithm
        int sum = 0;
        boolean alternate = false;
        for (int i = cleaned.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cleaned.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }

    private boolean isValidSSN(String ssn) {
        String cleaned = ssn.replaceAll("[^0-9]", "");

        if (cleaned.length() != 9) {
            return false;
        }

        // Basic validation: no all zeros, 666, or 900-999 in area number
        String area = cleaned.substring(0, 3);
        String group = cleaned.substring(3, 5);
        String serial = cleaned.substring(5, 9);

        if (area.equals("000") || area.equals("666") ||
                Integer.parseInt(area) >= 900) {
            return false;
        }

        if (group.equals("00") || serial.equals("0000")) {
            return false;
        }

        return true;
    }
}
