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
    // ========== PERSON NAME TESTS ==========

    @Test
    @DisplayName("Should detect simple person names")
    void testSimplePersonNames() {
        String[] names = {
                "Andy Lau",
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
                "Dr. Andy Lau",
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
                "Andy Lau works at the company.",
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
        String text = "Andy Lau and Mary Johnson met with Robert Williams " +
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
                "Patient Andy Lau was admitted on 2025-10-15.",
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
                System.out.println("No false positive: " + text);
            } else {
                System.out.println("False positive in: " + text);
                for (PIIEntity person : persons) {
                    System.out.println("  Incorrectly detected: " + person.getValue());
                }
            }
        }

        System.out.println("\nCorrectly ignored: " + correctlyIgnored + "/" + notNames.length);
    }

    @Test
    @DisplayName("Should detect first name only")
    void testFirstNameOnly() {
        String[] firstNames = {
                "John called yesterday.",
                "Mary sent the email.",
                "Robert will attend.",
                "Jennifer approved it."
        };

        System.out.println("\n=== First Name Only Detection ===");
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
    @DisplayName("Should detect names with suffixes")
    void testNamesWithSuffixes() {
        String[] namesWithSuffixes = {
                "Andy Lau Jr.",
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
                System.out.println("Found " + name + " -> " + persons.get(0).getValue());
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
        // International names vary in detection accuracy, need more tests
        String[] internationalNames = {
                "José García",
                "François Dubois",
                "Hans Mueller",
                "Li Wang",
                "Rajesh Kumar",
                "Nguyễn Văn Thiệu"
        };

        System.out.println("\n=== International Names ===");
        System.out.println("Note: Detection depends on Stanford NER's training data\n");
        int detected = 0;

        for (String name : internationalNames) {
            PIIDetectionResult result = detector.analyze(name);
            List<PIIEntity> persons = result.getEntitiesByType("PERSON");

            if (!persons.isEmpty()) {
                detected++;
                System.out.println("Found " + name + " -> " + persons.get(0).getValue());
            } else {
                System.out.println("Not found " + name);
            }
        }

        System.out.println("\nDetected: " + detected + "/" + internationalNames.length);
    }

    @Test
    @DisplayName("Should handle names at different positions")
    void testNamePositions() {
        String[] positions = {
                "Andy Lau started the project.",
                "The project was started by Andy Lau.",
                "In conclusion, Andy Lau recommends approval."
        };

        System.out.println("\n=== Names at Different Positions ===");
        int detected = 0;

        for (String text : positions) {
            PIIDetectionResult result = detector.analyze(text);
            List<PIIEntity> persons = result.getEntitiesByType("PERSON");

            if (!persons.isEmpty()) {
                detected++;
                System.out.println("Detected " + text);
                System.out.println("  Position: " + persons.get(0).getStartPosition());
            } else {
                System.out.println("Not detected " + text);
            }
            System.out.println();
        }

        assertEquals(3, detected, "Should detect names in all positions");
    }

    // -----------------
    @Test
    void testRedaction() {
        PIIDetector detector = new PIIDetector();

        String text = "Contact Andy Lau at andy@example.com or call 555-1234";
        String redacted = detector.redact(text);

        System.out.println("Original: " + text);
        System.out.println("Redacted: " + redacted);

        assertFalse(redacted.contains("andy@example.com"));
    }

    @Test
    @DisplayName("Should redact person names")
    void testPersonNameRedaction() {
        String text = "Andy Lau will meet with Mary Johnson tomorrow.";

        PIIDetectionResult result = detector.analyze(text);
        String redacted = result.getRedactedText();

        System.out.println("\nOriginal: " + text);
        System.out.println("Redacted: " + redacted);

        assertFalse(redacted.contains("Andy Lau"),
                "Redacted text should not contain first name");
        assertFalse(redacted.contains("Mary Johnson"),
                "Redacted text should not contain second name");
        assertTrue(redacted.contains("[PERSON]"),
                "Should contain [PERSON] marker");
    }

    // ========== ADDRESS TESTS ==========

    @Test
    @DisplayName("Should detect full US addresses")
    void testFullAddresses() {
        String[] addresses = {
                "123 Main Street, Springfield, IL 62701",
                "456 Oak Avenue, New York, NY 10001",
                "789 Elm Drive, Los Angeles, CA 90001",
                "321 Pine Road, Chicago, IL 60601",
                "654 Maple Lane, Boston, MA 02101"
        };

        System.out.println("\n=== Full Address Detection ===");
        int detected = 0;

        for (String address : addresses) {
            PIIDetectionResult result = detector.analyze(address);

            // Addresses may be detected as LOCATION entities or multiple components
            List<PIIEntity> locations = result.getEntitiesByType("LOCATION");
            List<PIIEntity> zipCodes = result.getEntitiesByType("ZIP_CODE");

            boolean hasLocation = !locations.isEmpty();
            boolean hasZip = !zipCodes.isEmpty();

            if (hasLocation || hasZip) {
                detected++;
                System.out.println("Input Address: " + address);
                System.out.println("  Locations found: " + locations.size());
                for (PIIEntity loc : locations) {
                    System.out.println("    - " + loc.getValue());
                }
                System.out.println("  ZIP codes found: " + zipCodes.size());
                for (PIIEntity zip : zipCodes) {
                    System.out.println("    - " + zip.getValue());
                }
            } else {
                System.out.println("Address not found: " + address);
            }
            System.out.println();
        }

        System.out.println("Detected: " + detected + "/" + addresses.length);
        assertTrue(detected >= 5,
                "Should detect at least 5 addresses (as locations or components)");
    }

    @Test
    @DisplayName("Unable to detect street addresses")
    void testStreetAddresses() {
        String[] streetAddresses = {
                "123 Main Street",
                "456 Oak Avenue",
                "789 Elm Drive",
                "321 Pine Road",
                "654 Maple Lane"
        };

        System.out.println("\n=== Street Address Detection ===");
        System.out.println("Note: Cannot detect Street addresses alone without city/state context\n");

        int detected = 0;

        for (String address : streetAddresses) {
            PIIDetectionResult result = detector.analyze(address);
            List<PIIEntity> locations = result.getEntitiesByType("LOCATION");

            if (!locations.isEmpty()) {
                detected++;
                System.out.println("Detected " + address + " -> " + locations.get(0).getValue());
            } else {
                System.out.println("Not detected " + address);
            }
        }

        System.out.println("\nDetected: " + detected + "/" + streetAddresses.length);
    }

    @Test
    @DisplayName("Should detect addresses in sentences")
    void testAddressesInSentences() {
        String[] sentences = {
                "Please send mail to 123 Main Street, Springfield, IL 62701.",
                "The office is located at 456 Oak Avenue, New York, NY 10001.",
                "He lives at 789 Elm Drive, Los Angeles, CA 90001.",
                "Contact us at 321 Pine Road, Chicago, IL 60601.",
                "Visit our store at 654 Maple Lane, Boston, MA 02101."
        };

        System.out.println("\n=== Addresses in Sentences ===");
        int detected = 0;

        for (String sentence : sentences) {
            PIIDetectionResult result = detector.analyze(sentence);

            List<PIIEntity> locations = result.getEntitiesByType("LOCATION");
            List<PIIEntity> zipCodes = result.getEntitiesByType("ZIP_CODE");

            if (!locations.isEmpty() || !zipCodes.isEmpty()) {
                detected++;
                System.out.println("Input Sentence: " + sentence);
                System.out.println("  Components found:");
                for (PIIEntity loc : locations) {
                    System.out.println("    Location: " + loc.getValue());
                }
                for (PIIEntity zip : zipCodes) {
                    System.out.println("    ZIP: " + zip.getValue());
                }
            } else {
                System.out.println("Location/zip not found in sentence: " + sentence);
            }
            System.out.println();
        }

        assertTrue(detected >= 4,
                "Should detect address components in at least 4 sentences");
    }

    @Test
    @DisplayName("Should detect city and state")
    void testCityState() {
        String[] cityStates = {
                "Springfield, IL",
                "New York, NY",
                "Los Angeles, CA",
                "Chicago, IL",
                "Boston, MA"
        };

        System.out.println("\n=== City, State Detection ===");
        int detected = 0;

        for (String cityState : cityStates) {
            PIIDetectionResult result = detector.analyze(cityState);
            List<PIIEntity> locations = result.getEntitiesByType("LOCATION");

            if (!locations.isEmpty()) {
                detected++;
                System.out.println("Detected " + cityState);
                for (PIIEntity loc : locations) {
                    System.out.println("  Detected: " + loc.getValue());
                }
            } else {
                System.out.println("Not detected " + cityState);
            }
            System.out.println();
        }

        System.out.println("Detected: " + detected + "/" + cityStates.length);
        assertTrue(detected >= 5, "Should detect at least 5 city/state combinations");
    }

    @Test
    @DisplayName("Should detect ZIP codes in addresses")
    void testZIPCodesInAddresses() {
        String[] addressesWithZip = {
                "123 Main St, Springfield, IL 62701",
                "456 Oak Ave, New York, NY 10001",
                "789 Elm Dr, Los Angeles, CA 90001-1234",  // ZIP+4
                "321 Pine Rd, Chicago, IL 60601"
        };

        System.out.println("\n=== ZIP Code Detection in Addresses ===");
        int detected = 0;

        for (String address : addressesWithZip) {
            PIIDetectionResult result = detector.analyze(address);
            List<PIIEntity> zipCodes = result.getEntitiesByType("ZIP_CODE");

            if (!zipCodes.isEmpty()) {
                detected++;
                System.out.println("Detected Address: " + address);
                System.out.println("  ZIP: " + zipCodes.get(0).getValue());
            } else {
                System.out.println("Not detected Address: " + address);
            }
            System.out.println();
        }

        assertEquals(4, detected, "Should detect all 4 ZIP codes");
    }

    @Test
    @DisplayName("Should detect multiple addresses in text")
    void testMultipleAddresses() {
        String text = "Ship to 123 Main Street, Springfield, IL 62701 " +
                "and bill to 456 Oak Avenue, New York, NY 10001.";

        PIIDetectionResult result = detector.analyze(text);

        List<PIIEntity> locations = result.getEntitiesByType("LOCATION");
        List<PIIEntity> zipCodes = result.getEntitiesByType("ZIP_CODE");

        System.out.println("\nInput: " + text);
        System.out.println("\nDetected " + locations.size() + " locations:");
        for (PIIEntity loc : locations) {
            System.out.println("  - " + loc.getValue());
        }
        System.out.println("\nDetected " + zipCodes.size() + " ZIP codes:");
        for (PIIEntity zip : zipCodes) {
            System.out.println("  - " + zip.getValue());
        }

        assertTrue(locations.size() >= 2 || zipCodes.size() >= 2,
                "Should detect components from multiple addresses");
    }

    @Test
    @DisplayName("Should handle PO Box addresses")
    void testPOBoxAddresses() {
        String[] poBoxes = {
                "PO Box 1234, Springfield, IL 62701",
                "P.O. Box 5678, New York, NY 10001",
                "Post Office Box 9012, Chicago, IL 60601"
        };

        System.out.println("\n=== PO Box Address Detection ===");
        int detected = 0;

        for (String poBox : poBoxes) {
            PIIDetectionResult result = detector.analyze(poBox);

            List<PIIEntity> locations = result.getEntitiesByType("LOCATION");
            List<PIIEntity> zipCodes = result.getEntitiesByType("ZIP_CODE");
            System.out.println("\nInput: " + poBox);
            System.out.println("\nDetected " + locations.size() + " locations:");
            for (PIIEntity loc : locations) {
                System.out.println("  - " + loc.getValue());
            }
            System.out.println("\nDetected " + zipCodes.size() + " ZIP codes:");
            for (PIIEntity zip : zipCodes) {
                System.out.println("  - " + zip.getValue());
            }


            if (!locations.isEmpty() || !zipCodes.isEmpty()) {
                detected++;

                System.out.println("\nInput: " + poBox);
                System.out.println("\nDetected " + locations.size() + " locations:");
                if (!locations.isEmpty()) {
                    System.out.println("  Location: " + locations.get(0).getValue());
                }
                System.out.println("\nDetected " + zipCodes.size() + " ZIP codes:");
                if (!zipCodes.isEmpty()) {
                    System.out.println("  ZIP: " + zipCodes.get(0).getValue());
                }
            } else {
                System.out.println("Not detected " + poBox);
            }
            System.out.println();
        }

        // PO Box detection can be hit-or-miss
        assertTrue(detected >= 2, "Should detect at least 2 PO Box addresses");
    }

    @Test
    @DisplayName("Should detect addresses with apartment numbers")
    void testApartmentAddresses() {
        String[] apartments = {
                "123 Main Street Apt 4B, Springfield, IL 62701",
                "456 Oak Avenue Unit 12, New York, NY 10001",
                "789 Elm Drive #5C, Los Angeles, CA 90001",
                "321 Pine Road Suite 200, Chicago, IL 60601"
        };

        System.out.println("\n=== Apartment Address Detection ===");
        int detected = 0;

        for (String apartment : apartments) {
            PIIDetectionResult result = detector.analyze(apartment);

            List<PIIEntity> locations = result.getEntitiesByType("LOCATION");
            List<PIIEntity> zipCodes = result.getEntitiesByType("ZIP_CODE");

            if (!locations.isEmpty() || !zipCodes.isEmpty()) {
                detected++;
                System.out.println("Detected " + apartment);
                System.out.println("\nDetected " + locations.size() + " locations:");
                for (PIIEntity loc : locations) {
                    System.out.println("  - " + loc.getValue());
                }
                System.out.println("\nDetected " + zipCodes.size() + " ZIP codes:");
                for (PIIEntity zip : zipCodes) {
                    System.out.println("  - " + zip.getValue());
                }
            } else {
                System.out.println("✗ " + apartment);
            }
        }

        System.out.println("\nDetected: " + detected + "/" + apartments.length);
        assertTrue(detected >= 3, "Should detect at least 3 apartment addresses");
    }

    @Test
    @DisplayName("Should detect international addresses")
    void testInternationalAddresses() {
        String[] internationalAddresses = {
                "10 Downing Street, London, UK",
                "1600 Pennsylvania Avenue, Washington DC",
                "Champs-Élysées, Paris, France",
                "Shibuya, Tokyo, Japan"
        };

        System.out.println("\n=== International Address Detection ===");
        int detected = 0;

        for (String address : internationalAddresses) {
            PIIDetectionResult result = detector.analyze(address);
            List<PIIEntity> locations = result.getEntitiesByType("LOCATION");

            if (!locations.isEmpty()) {
                detected++;
                System.out.println("Detected " + address);
                System.out.println("\nDetected " + locations.size() + " locations:");
                for (PIIEntity loc : locations) {
                    System.out.println("  - " + loc.getValue());
                }
            } else {
                System.out.println("Not detected " + address);
            }
            System.out.println();
        }

        System.out.println("Detected: " + detected + "/" + internationalAddresses.length);
    }


    @Test
    @DisplayName("Should redact PII in addresses")
    void testAddressRedaction() {
        String text = "Please send the package to Andy Lau at " +
                "123 Main Street, Springfield, IL 62701. call 1-555-123-4567 if questions.";
        System.out.println("\n=== Redact address info ===");

        PIIDetectionResult result = detector.analyze(text);
        String redacted = result.getRedactedText();

        System.out.println("\nOriginal: " + text);
        System.out.println("Redacted: " + redacted);
        System.out.println("\n");

        // Check that address components are redacted
        assertFalse(redacted.contains("Springfield"),
                "City should be redacted");
        assertFalse(redacted.contains("62701"),
                "ZIP code should be redacted");
        assertFalse(redacted.contains("Andy Lau"),
                "Name should be redacted");
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
		testCases.put("Call me at (555) 123-4567 today january 15th or tomorrow 1/16", "Embedded in sentence");
		testCases.put("Mobile: 555-123-4567", "With label");
		testCases.put("Ph: (555) 123-4567", "With abbreviation");

		System.out.println("\n=== Phone Number Format Detection Test ===\n");
        System.out.println("Use regex patterns for US phone number detection.\n");

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
				System.out.println("Detect: " + description);
				System.out.println("  Input:  " + phoneText);
				System.out.println("  Detected:  " + phones.get(0).getValue());
				System.out.println();
			} else {
				notDetected++;
				System.out.println("MISSED: " + description);
				System.out.println("  Input:  " + phoneText);
				System.out.println();
			}
		}

		System.out.println("=".repeat(50));
		System.out.println("Summary:");
		System.out.println("  Total tests: " + totalTests);
		System.out.println("  Detected:    " + detected);
		System.out.println("  Missed:      " + notDetected);
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

        System.out.println("\n=== Multiple Phone Numbers Detection ===\n");
        System.out.println("Input: " + text);
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
        System.out.println("NOTE: need more work on the regex to avoid false positives\n");

        for (String invalidPhone : invalidPhones) {
            PIIDetectionResult result = detector.analyze(invalidPhone);
            List<PIIEntity> phones = result.getEntitiesByType("PHONE");

            System.out.println("Input: " + invalidPhone);

            if (!phones.isEmpty()) {
                System.out.println(" INCORRECTLY detected as PHONE");
                for (PIIEntity phone : phones) {
                    System.out.println("  - " + phone.getValue());
                }
            } else {
                System.out.println(" Correctly NOT detected as PHONE");
            }

            System.out.println();
        }
    }

	@Test
	void testInternationalPhoneFormats() {
		PIIDetector detector = new PIIDetector();

		Map<String, String> internationalFormats = new LinkedHashMap<>();
		internationalFormats.put("+1-555-123-4567", "US with country code");

		// UK formats
		internationalFormats.put("+44 20 7123 4567", "UK London");
		internationalFormats.put("+44 7911 123456", "UK Mobile");

		// Other common formats
		internationalFormats.put("+81 3-1234-5678", "Japan");
		internationalFormats.put("+49 30 12345678", "Germany");
		internationalFormats.put("+33 1 42 86 82 00", "France");

		System.out.println("\n=== International Phone Format Test ===\n");
		System.out.println("Note: Our regex can only detect US formats");

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
	@Test
	@DisplayName("Should detect multiple SSNs in text")
	void testMultipleSSNs() {
		PIIDetector detector = new PIIDetector();

		// Use valid area codes (001-899, excluding 666)
		String text = "Process 123-45-6789 and 456-78-9012 today or tomorrow";
		PIIDetectionResult result = detector.analyze(text);

		System.out.println("\n=== All Detected Entities ===");
		System.out.println("Input: " + text);
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
				"and 456-78-9012 hired on 02/20/2020";

		PIIDetectionResult result = detector.analyze(text);

		System.out.println("\n=== All Detected Entities ===");
		System.out.println("\nNOTE: This uses regex-based detection = Low precision/confidence (many false positives)\n");
		System.out.println("Input: " + text + "\n");
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
	@Test
	@DisplayName("Should detect names with mixed PII")
	void testNamesWithOtherPII() {
		String text = "Andy Lau (SSN: 123-45-6789) can be reached at " +
				"andy.lau@example.com or (555) 123-4567.";

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
				"My account number is 1-4532-0151-1283-0366."
		};

		System.out.println("\n=== Testing Valid Credit Cards ===");

		for (String validCard : validCards) {
			PIIDetectionResult result = detector.analyze(validCard);
			List<PIIEntity> cards = result.getEntitiesByType("CREDIT_CARD");

			System.out.println("Card: " + validCard);
			System.out.println("  Found CC: " + !cards.isEmpty());
			System.out.println("  Value: " + (cards.isEmpty() ? "N/A" : cards.get(0).getValue()));

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

	// -----------------

    @Test
    void testEmail() {
        // Create the main detector
        PIIDetector detector = new PIIDetector();

        // Simple test
        String text = "Email me at john@example.com";

        PIIDetectionResult result = detector.analyze(text);

		System.out.println("Input text: " + text);
        System.out.println("Found " + result.getEntities().size() + " PII entities:");
        for (PIIEntity entity : result.getEntities()) {
            System.out.println("  - " + entity.getType() + ": " + entity.getValue());
        }

        assertTrue(result.hasPII(), "Should detect email as PII");
    }


    @Test
    @DisplayName("Should handle mixed PII")
    void testAddressesWithMixedPII() {
        String text = "Contact Andy Lau at john@example.com or " +
                "123 Main Street, Springfield, IL 62701, " +
                "phone: (555) 123-4567, SSN: 123-45-6789.";

        PIIDetectionResult result = detector.analyze(text);

        System.out.println("\nInput Text: " + text);
        System.out.println("\n=== Detected All PII ===");

        for (PIIEntity entity : result.getEntities()) {
            System.out.println(entity.getType() + ": " + entity.getValue());
        }

        // Should detect various PII types
        assertFalse(result.getEntitiesByType("PERSON").isEmpty(),
                "Should detect person name");
        assertFalse(result.getEntitiesByType("EMAIL").isEmpty(),
                "Should detect email");
        assertFalse(result.getEntitiesByType("PHONE").isEmpty(),
                "Should detect phone");
        assertFalse(result.getEntitiesByType("SSN").isEmpty(),
                "Should detect SSN");

        // Address components
        boolean hasAddressComponents =
                !result.getEntitiesByType("LOCATION").isEmpty() ||
                        !result.getEntitiesByType("ZIP_CODE").isEmpty();

        assertTrue(hasAddressComponents, "Should detect address components");
    }

	// Comprehensive test with multiple PII types and redaction
	@Test
	void testAllPIIDetection() {
		PIIDetector detector = new PIIDetector();

		// Realistic document with multiple PII types in various formats
		String document =
				"PATIENT INTAKE FORM\n" +
						"===================\n\n" +

						"Patient Information:\n" +
						"Full Name: Dr. John Michael Smith Jr.\n" +
						"Also known as: Andy Hui, Lau Tak-wah\n" +
						"Social Security Number: 123-45-6789\n\n" +

						"Contact Details:\n" +
						"Primary Phone: (555) 123-4567\n" +
						"Secondary Phone: 555-987-6543\n" +
						"Mobile: +1-555-444-3333\n" +
						"Email: a.hui.lau@example.com\n" +
						"Alternate Email: alau99@healthcare.org\n\n" +

						"Address:\n" +
						"Home: 123 Main Street, Apt 4B, Springfield, IL 62701\n" +
						"Work: 456 Corporate Blvd, Suite 200, Chicago, IL 60601\n" +
						"Mailing: PO Box 9876, New York, NY 10001\n\n" +

						"Emergency Contact:\n" +
						"Name: Mary Elizabeth Johnson\n" +
						"Relationship: Spouse\n" +
						"Phone: 555.111.2222\n" +
						"Address: Same as patient\n\n" +

						"Payment Information:\n" +
						"Credit Card: 4532-0151-1283-0366 (Visa)\n" +
						"Cardholder: Andy Lau\n" +
						"Billing Address: 123 Main Street, Springfield, IL 62701\n\n" +

						"Additional Contacts:\n" +
						"Primary Physician: Dr. Sarah Williams, (555) 777-8888\n" +
						"Specialist: Robert Lee Johnson MD, 555-999-0000\n" +
						"Pharmacy: Springfield Pharmacy, 789 Oak Avenue, Springfield, IL 62701, (555) 222-3333\n\n" +

						"References:\n" +
						"Friend: Michael Davis, michael.davis@email.com, 555-444-5555\n" +
						"Colleague: Jennifer Brown at jennifer.b@company.com\n\n" +

						"Notes:\n" +
						"Patient relocated from Los Angeles, CA to Springfield, IL.\n" +
						"Previous SSN on file: 234-56-7890 (update required)\n" +
						"Former address: 999 Sunset Boulevard, Los Angeles, CA 90001\n";

		System.out.println("\n" + "=".repeat(80));
		System.out.println("TEST REDACT ALL PII: Comprehensive PII Detection & Redaction");
		System.out.println("=".repeat(80));

		// Analyze and redact
		PIIDetectionResult result = detector.analyze(document);
		String redactedDocument = result.getRedactedText();

		System.out.println("\n>>> ORIGINAL DOCUMENT <<<\n");
		System.out.println(document);

		System.out.println("\n" + "=".repeat(80));
		System.out.println(">>> REDACTED DOCUMENT <<<");
		System.out.println("=".repeat(80) + "\n");
		System.out.println(redactedDocument);

		System.out.println("\n" + "=".repeat(80));
		System.out.println("Total PII entities detected: " + result.getEntities().size());
		System.out.println("Processing time: " + result.getProcessingTimeMs() + "ms");
		System.out.println("=".repeat(80));

		// Assertions
		assertTrue(result.hasPII(), "Should detect PII");
		assertTrue(result.getEntities().size() > 20, "Should detect multiple PII entities");

		// Verify original PII is not in redacted document
		assertFalse(redactedDocument.contains("123-45-6789"), "SSN should be redacted");
		assertFalse(redactedDocument.contains("john.smith@example.com"), "Email should be redacted");
		assertFalse(redactedDocument.contains("4532-0151-1283-0366"), "Credit card should be redacted");
		assertFalse(redactedDocument.contains("(555) 123-4567"), "Phone should be redacted");

		System.out.println("\n DONE");
	}

} // the end