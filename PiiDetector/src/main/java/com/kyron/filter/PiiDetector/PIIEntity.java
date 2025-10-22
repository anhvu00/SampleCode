package com.kyron.filter.PiiDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// PIIEntity.java - Represents a detected PII item
public class PIIEntity {
    private final String type;
    private final String value;
    private final int startPosition;
    private final int endPosition;
    private final double confidence;

    public PIIEntity(String type, String value, int startPosition, int endPosition) {
        this(type, value, startPosition, endPosition, 1.0);
    }

    public PIIEntity(String type, String value, int startPosition, int endPosition, double confidence) {
        this.type = type;
        this.value = value;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.confidence = confidence;
    }

    // Getters
    public String getType() { return type; }
    public String getValue() { return value; }
    public int getStartPosition() { return startPosition; }
    public int getEndPosition() { return endPosition; }
    public double getConfidence() { return confidence; }

    @Override
    public String toString() {
        return String.format("PIIEntity{type='%s', value='%s', position=%d-%d, confidence=%.2f}",
                type, value, startPosition, endPosition, confidence);
    }
}

