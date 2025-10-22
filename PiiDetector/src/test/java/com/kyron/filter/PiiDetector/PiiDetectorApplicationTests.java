package com.kyron.filter.PiiDetector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PiiDetectorApplicationTests {
	private PIIDetector detector;

	@BeforeEach
	void setUp() {
		detector = new PIIDetector();
	}
	@Test
	void contextLoads() {
		// Spring Boot context test
	}

	@Test
	void testBasicPIIDetection() {
		// Create the main detector
		PIIDetector detector = new PIIDetector();

		// Simple test
		String text = "Email me at john@example.com";

		PIIDetectionResult result = detector.analyze(text);

		System.out.println("Found " + result.getEntities().size() + " PII entities:");
		for (PIIEntity entity : result.getEntities()) {
			System.out.println("  - " + entity.getType() + ": " + entity.getValue());
		}

		assertTrue(result.hasPII(), "Should detect email as PII");
	}

	// -----------------
	@Test
	// Should detect various phone number formats"
	void testPhoneNumberVariations() {
		PIIDetector detector = new PIIDetector();

		// Test cases with different phone formats
		Map<String, String> testCases = new LinkedHashMap<>();

		// Standard US formats
		testCases.put("(555) 123-4567", "Parentheses with dash");
		testCases.put("555-123-4567", "Dashes only");
		testCases.put("555.123.4567", "Dots only");
		testCases.put("5551234567", "No separators");
		testCases.put("555 123 4567", "Spaces only");

		// With country code
		testCases.put("+1 (555) 123-4567", "Country code with parentheses");
		testCases.put("+1-555-123-4567", "Country code with dashes");
		testCases.put("+1 555 123 4567", "Country code with spaces");
		testCases.put("1-555-123-4567", "Leading 1 with dashes");
		testCases.put("1 (555) 123-4567", "Leading 1 with parentheses");

		// Mixed formats
		testCases.put("(555)123-4567", "No space after parentheses");
		testCases.put("555-1234", "7 digits with dash");
		testCases.put("555.123.4567", "Dots");

		// Edge cases
		testCases.put("Call me at (555) 123-4567 today", "Embedded in sentence");
		testCases.put("Mobile: 555-123-4567", "With label");
		testCases.put("Ph: (555) 123-4567", "With abbreviation");

		System.out.println("\n=== Phone Number Format Detection Test ===\n");

		int totalTests = testCases.size();
		int detected = 0;
		int notDetected = 0;

		for (Map.Entry<String, String> testCase : testCases.entrySet()) {
			String phoneText = testCase.getKey();
			String description = testCase.getValue();

			PIIDetectionResult result = detector.analyze(phoneText);
			List<PIIEntity> phones = result.getEntitiesByType("PHONE");

			boolean found = !phones.isEmpty();

			if (found) {
				detected++;
				System.out.println("✓ DETECTED: " + description);
				System.out.println("  Input:  " + phoneText);
				System.out.println("  Found:  " + phones.get(0).getValue());
				System.out.println();
			} else {
				notDetected++;
				System.out.println("✗ MISSED: " + description);
				System.out.println("  Input:  " + phoneText);
				System.out.println();
			}
		}

		System.out.println("=".repeat(50));
		System.out.println("Summary:");
		System.out.println("  Total tests: " + totalTests);
		System.out.println("  Detected:    " + detected);
		System.out.println("  Missed:      " + notDetected);
		System.out.println("  Success rate: " + String.format("%.1f%%", (detected * 100.0 / totalTests)));
		System.out.println("=".repeat(50));

		// Assert that we detect at least the common formats
		assertTrue(detected >= 10,
				"Should detect at least 10 common phone formats, detected: " + detected);
	}

	@Test
	// Should detect multiple phone numbers in one text"
	void testMultiplePhoneNumbers() {
		PIIDetector detector = new PIIDetector();

		String text = "Office: (555) 123-4567, Mobile: 555-987-6543, " +
				"Fax: 555.111.2222, Home: +1-555-333-4444";

		PIIDetectionResult result = detector.analyze(text);
		List<PIIEntity> phones = result.getEntitiesByType("PHONE");

		System.out.println("\nDetected " + phones.size() + " phone numbers:");
		for (PIIEntity phone : phones) {
			System.out.println("  - " + phone.getValue());
		}

		assertTrue(phones.size() >= 3,
				"Should detect at least 3 phone numbers, found: " + phones.size());
	}

	@Test
	// Should NOT detect invalid phone numbers"
	void testInvalidPhoneNumbers() {
		PIIDetector detector = new PIIDetector();

		// These should NOT be detected as phone numbers
		String[] invalidPhones = {
				"123-45-6789",      // SSN format
				"12345",            // Too short
				"123456789012",     // Too long
				"000-000-0000",     // All zeros
				"111-111-1111",     // Unlikely pattern
		};

		System.out.println("\n=== Testing Invalid Phone Numbers ===\n");

		for (String invalidPhone : invalidPhones) {
			PIIDetectionResult result = detector.analyze(invalidPhone);
			List<PIIEntity> phones = result.getEntitiesByType("PHONE");

			System.out.println("Input: " + invalidPhone);
			System.out.println("Detected as PHONE: " + !phones.isEmpty());

			// Note: Some patterns might still match, depending on regex strictness
			// This test documents current behavior
			System.out.println();
		}
	}

	@Test
	// Should handle international phone formats"
	void testInternationalPhoneFormats() {
		PIIDetector detector = new PIIDetector();

		Map<String, String> internationalFormats = new LinkedHashMap<>();

		// US formats (already tested above, but included for completeness)
		internationalFormats.put("+1-555-123-4567", "US with country code");

		// UK formats (if you want to support them in future)
		internationalFormats.put("+44 20 7123 4567", "UK London");
		internationalFormats.put("+44 7911 123456", "UK Mobile");

		// Other common formats
		internationalFormats.put("+81 3-1234-5678", "Japan");
		internationalFormats.put("+49 30 12345678", "Germany");
		internationalFormats.put("+33 1 42 86 82 00", "France");

		System.out.println("\n=== International Phone Format Test ===\n");
		System.out.println("Note: Current implementation focuses on US formats");
		System.out.println("This test documents what gets detected:\n");

		for (Map.Entry<String, String> format : internationalFormats.entrySet()) {
			String phone = format.getKey();
			String country = format.getValue();

			PIIDetectionResult result = detector.analyze(phone);
			List<PIIEntity> phones = result.getEntitiesByType("PHONE");

			System.out.println(country + ": " + phone);
			System.out.println("  Detected: " + !phones.isEmpty());
			if (!phones.isEmpty()) {
				System.out.println("  Captured: " + phones.get(0).getValue());
			}
			System.out.println();
		}
	}

	// -----------------


	// -----------------
	@Test
	void testRedaction() {
		PIIDetector detector = new PIIDetector();

		String text = "Contact John Smith at john@example.com or call 555-1234";
		String redacted = detector.redact(text);

		System.out.println("Original: " + text);
		System.out.println("Redacted: " + redacted);

		assertFalse(redacted.contains("john@example.com"));
	}

	// -----------------
	@Test
	@DisplayName("Should detect multiple SSNs in text")
	void testMultipleSSNs() {
		PIIDetector detector = new PIIDetector();

		// Use valid area codes (001-899, excluding 666)
		String text = "Process 123-45-6789 and 456-78-9012 today";
		PIIDetectionResult result = detector.analyze(text);

		// DEBUG: See all detected entities
		System.out.println("\n=== All Detected Entities ===");
		for (PIIEntity entity : result.getEntities()) {
			System.out.println(entity.getType() + ": " + entity.getValue() +
					" (pos: " + entity.getStartPosition() + "-" + entity.getEndPosition() + ")");
		}

		List<PIIEntity> ssns = result.getEntitiesByType("SSN");
		System.out.println("\nSSNs found: " + ssns.size());
		for (PIIEntity ssn : ssns) {
			System.out.println("  - " + ssn.getValue());
		}

		assertEquals(2, ssns.size(), "Should detect 2 SSNs");
	}

	@Test
	@DisplayName("Should detect valid SSNs in mixed text")
	void testSSNInMixedText() {
		PIIDetector detector = new PIIDetector();

		// Use valid SSNs
		String text = "Employee records: 123-45-6789 hired on 2020-01-15 " +
				"and 456-78-9012 hired on 2020-02-20";

		PIIDetectionResult result = detector.analyze(text);

		// DEBUG: See all detected entities
		System.out.println("\n=== All Detected Entities ===");
		for (PIIEntity entity : result.getEntities()) {
			System.out.println(entity.getType() + ": " + entity.getValue() +
					" (pos: " + entity.getStartPosition() + "-" + entity.getEndPosition() + ")");
		}

		List<PIIEntity> ssns = result.getEntitiesByType("SSN");
		System.out.println("\nSSNs found: " + ssns.size());
		for (PIIEntity ssn : ssns) {
			System.out.println("  - " + ssn.getValue());
		}

		assertEquals(2, ssns.size(), "Should find 2 SSNs in text");
	}

	// -----------------

	// ========== PERSON NAME TESTS ==========

	@Test
	@DisplayName("Should detect simple person names")
	void testSimplePersonNames() {
		String[] names = {
				"John Smith",
				"Mary Johnson",
				"Robert Williams",
				"Jennifer Brown",
				"Michael Davis"
		};

		System.out.println("\n=== Simple Person Name Detection ===");
		int detected = 0;

		for (String name : names) {
			PIIDetectionResult result = detector.analyze(name);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (!persons.isEmpty()) {
				detected++;
				System.out.println("✓ Detected: " + name + " -> " + persons.get(0).getValue());
			} else {
				System.out.println("✗ Missed: " + name);
			}
		}

		System.out.println("\nDetected: " + detected + "/" + names.length);
		assertTrue(detected >= 4, "Should detect at least 4 person names");
	}

	@Test
	@DisplayName("Should detect names with titles")
	void testNamesWithTitles() {
		String[] namesWithTitles = {
				"Dr. John Smith",
				"Mr. Robert Johnson",
				"Ms. Mary Williams",
				"Mrs. Jennifer Davis",
				"Prof. Michael Brown"
		};

		System.out.println("\n=== Names with Titles ===");
		int detected = 0;

		for (String nameWithTitle : namesWithTitles) {
			PIIDetectionResult result = detector.analyze(nameWithTitle);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (!persons.isEmpty()) {
				detected++;
				System.out.println("✓ " + nameWithTitle);
				System.out.println("  Detected: " + persons.get(0).getValue());
			} else {
				System.out.println("✗ " + nameWithTitle);
			}
		}

		System.out.println("\nDetected: " + detected + "/" + namesWithTitles.length);
		assertTrue(detected >= 3, "Should detect at least 3 names with titles");
	}

	@Test
	@DisplayName("Should detect names in sentences")
	void testNamesInSentences() {
		String[] sentences = {
				"John Smith works at the company.",
				"The report was written by Mary Johnson.",
				"Please contact Robert Williams for details.",
				"Jennifer Brown will attend the meeting.",
				"Michael Davis is the project manager."
		};

		System.out.println("\n=== Names in Sentences ===");
		int detected = 0;

		for (String sentence : sentences) {
			PIIDetectionResult result = detector.analyze(sentence);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (!persons.isEmpty()) {
				detected++;
				System.out.println("✓ " + sentence);
				System.out.println("  Detected: " + persons.get(0).getValue());
			} else {
				System.out.println("✗ " + sentence);
			}
		}

		System.out.println("\nDetected: " + detected + "/" + sentences.length);
		assertTrue(detected >= 4, "Should detect at least 4 names in sentences");
	}

	@Test
	@DisplayName("Should detect multiple names in one text")
	void testMultipleNames() {
		String text = "John Smith and Mary Johnson met with Robert Williams " +
				"to discuss the project. Jennifer Brown joined later.";

		PIIDetectionResult result = detector.analyze(text);
		List<PIIEntity> persons = result.getEntitiesByType("PERSON");

		System.out.println("\nText: " + text);
		System.out.println("\nDetected " + persons.size() + " person names:");
		for (PIIEntity person : persons) {
			System.out.println("  - " + person.getValue() +
					" (pos: " + person.getStartPosition() + "-" + person.getEndPosition() + ")");
		}

		assertTrue(persons.size() >= 3,
				"Should detect at least 3 names, found: " + persons.size());
	}

	@Test
	@DisplayName("Should detect names with middle initials")
	void testNamesWithMiddleInitials() {
		String[] namesWithMiddle = {
				"John Q. Public",
				"Mary K. Smith",
				"Robert E. Lee",
				"Jennifer L. Johnson"
		};

		System.out.println("\n=== Names with Middle Initials ===");
		int detected = 0;

		for (String name : namesWithMiddle) {
			PIIDetectionResult result = detector.analyze(name);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (!persons.isEmpty()) {
				detected++;
				System.out.println("✓ " + name + " -> " + persons.get(0).getValue());
			} else {
				System.out.println("✗ " + name);
			}
		}

		System.out.println("\nDetected: " + detected + "/" + namesWithMiddle.length);
		// Middle initials can be tricky for NER
		assertTrue(detected >= 2, "Should detect at least 2 names with middle initials");
	}

	@Test
	@DisplayName("Should detect full names (first, middle, last)")
	void testFullNames() {
		String[] fullNames = {
				"John Michael Smith",
				"Mary Elizabeth Johnson",
				"Robert James Williams"
		};

		System.out.println("\n=== Full Names (First Middle Last) ===");
		int detected = 0;

		for (String name : fullNames) {
			PIIDetectionResult result = detector.analyze(name);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (!persons.isEmpty()) {
				detected++;
				System.out.println("✓ " + name + " -> " + persons.get(0).getValue());
			} else {
				System.out.println("✗ " + name);
			}
		}

		System.out.println("\nDetected: " + detected + "/" + fullNames.length);
		assertTrue(detected >= 2, "Should detect at least 2 full names");
	}

	@Test
	@DisplayName("Should detect names in realistic contexts")
	void testNamesInRealisticContexts() {
		String[] contexts = {
				"Patient John Smith was admitted on 2024-01-15.",
				"The application from Mary Johnson has been approved.",
				"Email from Robert Williams received at 3:00 PM.",
				"Invoice submitted by Jennifer Brown for $1,500.",
				"Dr. Michael Davis will see you on Tuesday."
		};

		System.out.println("\n=== Names in Realistic Contexts ===");
		int detected = 0;

		for (String context : contexts) {
			PIIDetectionResult result = detector.analyze(context);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (!persons.isEmpty()) {
				detected++;
				System.out.println("✓ Context: " + context);
				System.out.println("  Detected: " + persons.get(0).getValue());
			} else {
				System.out.println("✗ Context: " + context);
			}
			System.out.println();
		}

		assertTrue(detected >= 4, "Should detect at least 4 names in contexts");
	}

	@Test
	@DisplayName("Should NOT detect common words as names")
	void testFalsePositives() {
		String[] notNames = {
				"The meeting will start soon.",
				"Please review the document.",
				"Send the report by Friday.",
				"Check the email address.",
				"Update the database records."
		};

		System.out.println("\n=== Testing False Positives ===");
		int correctlyIgnored = 0;

		for (String text : notNames) {
			PIIDetectionResult result = detector.analyze(text);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (persons.isEmpty()) {
				correctlyIgnored++;
				System.out.println("✓ No false positive: " + text);
			} else {
				System.out.println("✗ False positive in: " + text);
				for (PIIEntity person : persons) {
					System.out.println("  Incorrectly detected: " + person.getValue());
				}
			}
		}

		System.out.println("\nCorrectly ignored: " + correctlyIgnored + "/" + notNames.length);
	}

	@Test
	@DisplayName("Should detect names with suffixes")
	void testNamesWithSuffixes() {
		String[] namesWithSuffixes = {
				"John Smith Jr.",
				"Robert Williams Sr.",
				"Michael Brown III",
				"James Davis Jr"
		};

		System.out.println("\n=== Names with Suffixes ===");
		int detected = 0;

		for (String name : namesWithSuffixes) {
			PIIDetectionResult result = detector.analyze(name);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (!persons.isEmpty()) {
				detected++;
				System.out.println("✓ " + name + " -> " + persons.get(0).getValue());
			} else {
				System.out.println("✗ " + name);
			}
		}

		System.out.println("\nDetected: " + detected + "/" + namesWithSuffixes.length);
		// Suffixes can be challenging
		assertTrue(detected >= 2, "Should detect at least 2 names with suffixes");
	}

	@Test
	@DisplayName("Should handle international names")
	void testInternationalNames() {
		String[] internationalNames = {
				"José García",
				"François Dubois",
				"Hans Mueller",
				"Li Wang",
				"Rajesh Kumar"
		};

		System.out.println("\n=== International Names ===");
		System.out.println("Note: Detection depends on Stanford NER's training data\n");
		int detected = 0;

		for (String name : internationalNames) {
			PIIDetectionResult result = detector.analyze(name);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (!persons.isEmpty()) {
				detected++;
				System.out.println("✓ " + name + " -> " + persons.get(0).getValue());
			} else {
				System.out.println("✗ " + name);
			}
		}

		System.out.println("\nDetected: " + detected + "/" + internationalNames.length);
		// International names vary in detection accuracy
	}

	@Test
	@DisplayName("Should detect names with mixed PII")
	void testNamesWithOtherPII() {
		String text = "John Smith (SSN: 123-45-6789) can be reached at " +
				"john.smith@example.com or (555) 123-4567.";

		PIIDetectionResult result = detector.analyze(text);

		System.out.println("\nText: " + text);
		System.out.println("\n=== All Detected PII ===");
		for (PIIEntity entity : result.getEntities()) {
			System.out.println(entity.getType() + ": " + entity.getValue());
		}

		List<PIIEntity> persons = result.getEntitiesByType("PERSON");
		assertFalse(persons.isEmpty(), "Should detect person name");

		// Should also detect other PII types
		assertFalse(result.getEntitiesByType("SSN").isEmpty());
		assertFalse(result.getEntitiesByType("EMAIL").isEmpty());
	}

	@Test
	@DisplayName("Should redact person names")
	void testPersonNameRedaction() {
		String text = "John Smith will meet with Mary Johnson tomorrow.";

		PIIDetectionResult result = detector.analyze(text);
		String redacted = result.getRedactedText();

		System.out.println("\nOriginal: " + text);
		System.out.println("Redacted: " + redacted);

		assertFalse(redacted.contains("John Smith"),
				"Redacted text should not contain first name");
		assertFalse(redacted.contains("Mary Johnson"),
				"Redacted text should not contain second name");
		assertTrue(redacted.contains("[PERSON]"),
				"Should contain [PERSON] marker");
	}

	@Test
	@DisplayName("Should detect first name only")
	void testFirstNameOnly() {
		// First names alone are often not detected by NER
		// This test documents current behavior
		String[] firstNames = {
				"John called yesterday.",
				"Mary sent the email.",
				"Robert will attend.",
				"Jennifer approved it."
		};

		System.out.println("\n=== First Name Only Detection ===");
		System.out.println("Note: Single names are harder to detect\n");
		int detected = 0;

		for (String text : firstNames) {
			PIIDetectionResult result = detector.analyze(text);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			System.out.println("Text: " + text);
			System.out.println("  Detected: " + !persons.isEmpty());
			if (!persons.isEmpty()) {
				detected++;
				System.out.println("  Value: " + persons.get(0).getValue());
			}
			System.out.println();
		}


	}

	@Test
	@DisplayName("Should handle names at different positions")
	void testNamePositions() {
		String[] positions = {
				"John Smith started the project.",
				"The project was started by John Smith.",
				"In conclusion, John Smith recommends approval."
		};

		System.out.println("\n=== Names at Different Positions ===");
		int detected = 0;

		for (String text : positions) {
			PIIDetectionResult result = detector.analyze(text);
			List<PIIEntity> persons = result.getEntitiesByType("PERSON");

			if (!persons.isEmpty()) {
				detected++;
				System.out.println("✓ " + text);
				System.out.println("  Position: " + persons.get(0).getStartPosition());
			} else {
				System.out.println("✗ " + text);
			}
			System.out.println();
		}

		assertEquals(3, detected, "Should detect names in all positions");
	}

	// -----------------
	// credit card number tests
	// ========== CREDIT CARD TESTS ==========

	@Test
	@DisplayName("Should detect valid credit card with Luhn")
	void testCreditCardDetection() {
		// These are VALID credit cards (pass Luhn check)
		String[] validCards = {
				"4532015112830366",      // Visa
				"5425233430109903",      // Mastercard
				"374245455400126",       // Amex (15 digits)
				"6011000991300009",      // Discover
		};

		System.out.println("\n=== Testing Valid Credit Cards ===");

		for (String validCard : validCards) {
			PIIDetectionResult result = detector.analyze(validCard);
			List<PIIEntity> cards = result.getEntitiesByType("CREDIT_CARD");

			System.out.println("Card: " + validCard);
			System.out.println("  Detected: " + !cards.isEmpty());

			assertFalse(cards.isEmpty(), "Should detect valid credit card: " + validCard);
		}
	}

	@Test
	@DisplayName("Should reject invalid credit card")
	void testInvalidCreditCard() {
		// Invalid credit card (fails Luhn check)
		String invalidCard = "1234-5678-9012-3456";
		PIIDetectionResult result = detector.analyze(invalidCard);

		List<PIIEntity> cards = result.getEntitiesByType("CREDIT_CARD");
		assertEquals(0, cards.size(), "Should not detect invalid credit card");
	}


} // the end