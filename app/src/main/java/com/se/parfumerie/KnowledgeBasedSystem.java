package com.se.parfumerie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KnowledgeBasedSystem {
    private final Set<String> workingMemory;
    private final List<Rule> productionRules;

    public KnowledgeBasedSystem() {
        this.workingMemory = new HashSet<>();
        this.productionRules = new ArrayList<>();

    }

    public void addRule(Rule rule) {
        productionRules.add(rule);
    }

    public void addFact(String fact) {
        workingMemory.add(fact);
    }

    public boolean isWorkingMemoryEmpty() {
        return workingMemory.isEmpty();
    }

    public List<String> infer(Map<String, List<String>> fragrances) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule rule : productionRules) {
                if (rule.applies(workingMemory)) {
                    workingMemory.add(rule.getConclusion());
                    changed = true;
                }
            }
        }
        System.out.println(workingMemory);
        return pickFragrances(fragrances);
    }

    private List<String> pickFragrances(Map<String, List<String>> fragrances) {
        List<String> result = new ArrayList<>();
        int maxNumberOfSimilarities = 0;
        for (Map.Entry<String, List<String>> fragrance : fragrances.entrySet()) {
            int numberOfSimilarities = 0;
            for (String property : fragrance.getValue()) {
                if (workingMemory.contains(property)) {
                    numberOfSimilarities++;
                }
            }
            if (numberOfSimilarities > maxNumberOfSimilarities) {
                maxNumberOfSimilarities = numberOfSimilarities;
                result.clear();
                result.add(fragrance.getKey());
                System.out.println(maxNumberOfSimilarities + result.toString());
            } else if (numberOfSimilarities == maxNumberOfSimilarities) {
                result.add(fragrance.getKey());
                System.out.println(maxNumberOfSimilarities + result.toString());
            }
        }
        return result;
    }
}
