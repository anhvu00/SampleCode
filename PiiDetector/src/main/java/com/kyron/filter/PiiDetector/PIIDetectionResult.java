package com.kyron.filter.PiiDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// PIIDetectionResult.java - Container for all detected entities
public class PIIDetectionResult {
    private final String originalText;
    private final List<PIIEntity> entities;
    private final long processingTimeMs;

    public PIIDetectionResult(String originalText, List<PIIEntity> entities, long processingTimeMs) {
        this.originalText = originalText;
        this.entities = new ArrayList<>(entities);
        this.processingTimeMs = processingTimeMs;
    }

    public List<PIIEntity> getEntities() {
        return new ArrayList<>(entities);
    }

    public List<PIIEntity> getEntitiesByType(String type) {
        return entities.stream()
                .filter(e -> e.getType().equals(type))
                .collect(Collectors.toList());
    }

    public boolean hasPII() {
        return !entities.isEmpty();
    }

    public String getRedactedText() {
        return getRedactedText("[REDACTED]");
    }

    public String getRedactedText(String placeholder) {
        StringBuilder redacted = new StringBuilder(originalText);

        // Sort entities by position in reverse order to maintain indices
        List<PIIEntity> sorted = new ArrayList<>(entities);
        sorted.sort((a, b) -> Integer.compare(b.getStartPosition(), a.getStartPosition()));

        for (PIIEntity entity : sorted) {
            String replacement = placeholder.equals("[REDACTED]")
                    ? "[" + entity.getType() + "]"
                    : placeholder;
            redacted.replace(entity.getStartPosition(), entity.getEndPosition(), replacement);
        }

        return redacted.toString();
    }

    public String getOriginalText() { return originalText; }
    public long getProcessingTimeMs() { return processingTimeMs; }
}