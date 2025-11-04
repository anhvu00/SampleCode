# PII Detector

🤖 **AI-assisted PII detection and filtering for Java**

Combines machine learning (Stanford CoreNLP NER) with rule-based pattern matching for comprehensive, on-premise PII identification and redaction.

## Features

- 🔍 Detects 6 core PII types: Names, Addresses, Phone Numbers, SSN, Email, Credit Cards
- 🤖 AI-assisted entity detection (Stanford CoreNLP NER)
- ✅ Validation (Luhn algorithm for credit cards, SSN format validation)
- 🔒 On-premise processing (no cloud dependencies)
- ⚡ Java-based with Spring Boot compatibility
- 🧪 Comprehensive test suite (29 tests with JUnit 5)

## Supported PII Types

| PII Type | Detection Method | Validation | Test Coverage | Notes |
|----------|-----------------|------------|---------------|-------|
| **Person Names** | Stanford NER (AI) | - | ✅ 12 tests | Best with full names (Andy Lau); single first names may not be detected |
| **Addresses** | Stanford NER (AI) + Regex | - | ✅ 5 tests | Detects cities, states, ZIP codes; street addresses detected via location context |
| **Phone Numbers** | Regex | Format validation | ✅ 2 tests | US formats: (555) 123-4567, 555-123-4567, 555.123.4567, +1-555-123-4567 |
| **SSN** | Regex | Area/Group/Serial validation | ✅ 7 tests | Formats: 123-45-6789, 123456789; Rejects invalid area (000, 666, 900+) |
| **Email** | Regex | Format validation | ✅ 1 test | Standard email formats: user@domain.com |
| **Credit Cards** | Regex | Luhn algorithm | ✅ 2 tests | 13-19 digits (Visa, Mastercard, Amex, Discover); validates with Luhn checksum |

**Total: 29 comprehensive tests**

## Architecture

### Hybrid Detection Approach

**AI/ML-Based Detection** (Stanford CoreNLP NER)
- Person names
- Locations (cities, states for address detection)

**Rule-Based Detection** (Regex + Validation)
- Social Security Numbers (with area/group/serial validation)
- Email addresses
- Phone numbers (US formats)
- Credit cards (with Luhn algorithm validation)
- ZIP codes (5-digit and ZIP+4)

## Requirements
- Java 11 or higher
- Maven 3.6+
- Stanford CoreNLP 4.5.5

## Dependencies
```xml
<dependencies>
    <!-- Stanford CoreNLP -->
    <dependency>
        <groupId>edu.stanford.nlp</groupId>
        <artifactId>stanford-corenlp</artifactId>
        <version>4.5.5</version>
    </dependency>
    <dependency>
        <groupId>edu.stanford.nlp</groupId>
        <artifactId>stanford-corenlp</artifactId>
        <version>4.5.5</version>
        <classifier>models</classifier>
    </dependency>
    
    <!-- Spring Boot (optional) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    
    <!-- JUnit 5 for testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Usage
```java
// Initialize detector
PIIDetector detector = new PIIDetector();

// Analyze text
String text = "Contact Andy Lau at john@example.com or 555-123-4567. " +
             "Address: 123 Main St, Springfield, IL 62701. SSN: 123-45-6789";

PIIDetectionResult result = detector.analyze(text);

// Get detected entities
for (PIIEntity entity : result.getEntities()) {
    System.out.println(entity.getType() + ": " + entity.getValue());
}

// Get redacted text
String redacted = result.getRedactedText();
System.out.println(redacted);
// Output: Contact [PERSON] at [EMAIL] or [PHONE]. 
//         Address: 123 Main St, [LOCATION], [LOCATION] [ZIP_CODE]. SSN: [SSN]
```

## Testing

Run all tests:
```bash
mvn test
```

## Test Coverage Details

### Email Tests (1 test)
- Basic email detection

### SSN Tests (7 tests)
- Format detection (with/without dashes)
- Multiple SSNs in text
- Invalid area number rejection
- Invalid group/serial rejection
- Detection in mixed text
- Redaction
- Valid edge cases

### Phone Tests (2 tests)
- Various US phone formats
- Multiple phone numbers in text

### Credit Card Tests (2 tests)
- Valid card detection (Luhn validation)
- Invalid card rejection

### Person Name Tests (12 tests)
- Simple names
- Names with titles (Dr., Mr., Ms.)
- Names in sentences
- Multiple names in text
- Names with middle initials
- Full names (first middle last)
- Names in realistic contexts
- False positive prevention
- Names with suffixes (Jr., Sr., III)
- International names
- Names with other PII
- Name redaction
- Names at different positions

### Address Tests (5 tests)
- Full addresses (street, city, state, ZIP)
- Addresses in context
- Address redaction
- City and state detection
- Comprehensive PII detection (all types together)

## Limitations - Future Enhancements
- Passport numbers
- Driver's license numbers
- Medical record numbers (MRN)
- Bank account numbers
- IP addresses
- URLs
- Dates (DOB?)
- Organizations (as PII)
- International addresses (variable accuracy)
- International phone numbers (non-US)

### Detection Challenges
- **Single first names** without context (e.g., "John called")
- **Street addresses** without city/state/ZIP context
- **Uncommon names** not in Stanford NER training data
- **Obfuscated PII** (e.g., "XXX-XX-1234" for partial SSN)
- **Non-English text** (This version of Stanford NER is trained on English)
- **Misspelled PII** (typos won't match patterns)

## Project Structure
```
src/
├── main/java/com/kyron/filter/PiiDetector/
│   ├── PIIDetector.java              # Main coordinator
│   ├── RegexPIIDetector.java         # Pattern-based detection
│   ├── StanfordNERDetector.java      # AI/NER detection
│   ├── PIIEntity.java                # Data model
│   └── PIIDetectionResult.java       # Result container
└── test/java/com/kyron/filter/PiiDetector/
    └── PiiDetectorApplicationTests.java  # Comprehensive test suite (29 tests)
```

## Performance Considerations

- **First run**: Stanford NER model loading takes 2-3 seconds
- **Subsequent runs**: Fast detection (typically < 100ms for short texts)
- **Recommendation**: Initialize `PIIDetector` once and reuse (singleton pattern)

## Example Output

**Input:**
```
Patient: Andy Lau
Address: 123 Main Street, Springfield, IL 62701
Phone: (555) 123-4567
Email: andy.lau@example.com
SSN: 123-45-6789
Credit Card: 4532-0151-1283-0366
```

**Detected PII:**
```
PERSON: Andy Lau
LOCATION: Springfield
LOCATION: IL
ZIP_CODE: 62701
PHONE: (555) 123-4567
EMAIL: andy.lau@example.com
SSN: 123-45-6789
CREDIT_CARD: 4532-0151-1283-0366
```

**Redacted Output:**
```
Patient: [PERSON]
Address: 123 Main Street, [LOCATION], [LOCATION] [ZIP_CODE]
Phone: [PHONE]
Email: [EMAIL]
SSN: [SSN]
Credit Card: [CREDIT_CARD]
```

the end.