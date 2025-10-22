package com.kyron.filter.PiiDetector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Main PII Detector that combines Regex and Stanford NER detection
 */
public class PIIDetector {

    private final RegexPIIDetector regexDetector;
    private final StanfordNERDetector nerDetector;

    public PIIDetector() {
        this.regexDetector = new RegexPIIDetector();
        this.nerDetector = new StanfordNERDetector();
    }

    /**
     * Analyze text and detect all PII entities
     * @param text The text to analyze
     * @return Detection result containing all found PII entities
     */
    public PIIDetectionResult analyze(String text) {
        long startTime = System.currentTimeMillis();

        List<PIIEntity> allEntities = new ArrayList<>();

        // 1. Run regex detection (fast, high precision for structured data)
        allEntities.addAll(regexDetector.detect(text));

        // 2. Run NER detection (catches names, locations, dates)
        allEntities.addAll(nerDetector.detect(text));

        // 3. Remove duplicates and overlapping entities
        List<PIIEntity> deduplicated = deduplicateEntities(allEntities);

        long endTime = System.currentTimeMillis();

        return new PIIDetectionResult(text, deduplicated, endTime - startTime);
    }

    /**
     * Remove overlapping entities, keeping the one with higher confidence
     */
    private List<PIIEntity> deduplicateEntities(List<PIIEntity> entities) {
        if (entities.isEmpty()) {
            return new ArrayList<>();
        }

        // Sort by start position
        List<PIIEntity> sorted = new ArrayList<>(entities);
        sorted.sort(Comparator.comparingInt(PIIEntity::getStartPosition));

        List<PIIEntity> result = new ArrayList<>();
        PIIEntity previous = null;

        for (PIIEntity current : sorted) {
            if (previous == null) {
                result.add(current);
                previous = current;
                continue;
            }

            // Check for overlap
            if (current.getStartPosition() < previous.getEndPosition()) {
                // Overlapping - keep the one with higher confidence
                if (current.getConfidence() > previous.getConfidence()) {
                    result.remove(result.size() - 1);
                    result.add(current);
                    previous = current;
                }
                // else: keep previous, skip current
            } else {
                // No overlap - add current
                result.add(current);
                previous = current;
            }
        }

        return result;
    }

    /**
     * Quick check if text contains any PII
     */
    public boolean containsPII(String text) {
        return analyze(text).hasPII();
    }

    /**
     * Get redacted version of text
     */
    public String redact(String text) {
        return analyze(text).getRedactedText();
    }

    /**
     * Get redacted version with custom placeholder
     */
    public String redact(String text, String placeholder) {
        return analyze(text).getRedactedText(placeholder);
    }
}
