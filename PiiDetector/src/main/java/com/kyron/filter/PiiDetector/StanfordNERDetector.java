package com.kyron.filter.PiiDetector;

// StanfordNERDetector.java
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// Name Entity Recognition using Stanford NLP
public class StanfordNERDetector {

    private final StanfordCoreNLP pipeline;

    public StanfordNERDetector() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");

        // DISABLE SUTime - causes loading issues
        props.setProperty("ner.useSUTime", "false");  // ← Changed from "true" to "false"
        props.setProperty("ner.applyFineGrained", "false");

        // Suppress some warnings
        props.setProperty("ner.applyNumericClassifiers", "false");

        // Initialize pipeline
        this.pipeline = new StanfordCoreNLP(props);
    }

    public List<PIIEntity> detect(String text) {
        List<PIIEntity> entities = new ArrayList<>();

        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);

            // Group consecutive tokens with same NER tag
            String currentNER = "";
            StringBuilder currentValue = new StringBuilder();
            int startPos = -1;

            for (int i = 0; i < tokens.size(); i++) {
                CoreLabel token = tokens.get(i);
                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                int tokenStart = token.beginPosition();
                int tokenEnd = token.endPosition();

                if (!ner.equals("O") && isPIIType(ner)) {
                    if (ner.equals(currentNER)) {
                        // Continue current entity
                        currentValue.append(" ").append(word);
                    } else {
                        // Save previous entity if exists
                        if (!currentNER.isEmpty()) {
                            entities.add(new PIIEntity(
                                    currentNER,
                                    currentValue.toString(),
                                    startPos,
                                    tokens.get(i - 1).endPosition(),
                                    0.85
                            ));
                        }
                        // Start new entity
                        currentNER = ner;
                        currentValue = new StringBuilder(word);
                        startPos = tokenStart;
                    }
                } else {
                    // Save previous entity if exists
                    if (!currentNER.isEmpty()) {
                        entities.add(new PIIEntity(
                                currentNER,
                                currentValue.toString(),
                                startPos,
                                tokens.get(i - 1).endPosition(),
                                0.85
                        ));
                        currentNER = "";
                        currentValue = new StringBuilder();
                    }
                }
            }

            // Don't forget last entity
            if (!currentNER.isEmpty()) {
                entities.add(new PIIEntity(
                        currentNER,
                        currentValue.toString(),
                        startPos,
                        tokens.get(tokens.size() - 1).endPosition(),
                        0.85
                ));
            }
        }

        return entities;
    }

    private boolean isPIIType(String nerTag) {
        // Stanford NER tags that represent PII
        return nerTag.equals("PERSON") ||
                nerTag.equals("LOCATION") ||
                nerTag.equals("ORGANIZATION") ||
                nerTag.equals("DATE") ||
                nerTag.equals("TIME") ||
                nerTag.equals("MONEY");
    }
}