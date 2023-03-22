package com.se.parfumerie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeBasedSystem {
    private final Map<String, Boolean> workingMemory;
    private final List<Rule> productionRules;

    public KnowledgeBasedSystem() {
        this.workingMemory = new HashMap<>();
        this.productionRules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        productionRules.add(rule);
    }

    public void addFact(String fact, boolean value) {
        workingMemory.put(fact, value);
    }

    public boolean infer(String goal) {
        boolean changed = true;
        while (changed) {
            changed = false;
            for (Rule rule : productionRules) {
                if (rule.applies(workingMemory) && !workingMemory.containsKey(rule.getConclusion())) {
                    workingMemory.put(rule.getConclusion(), true);
                    if (rule.getConclusion().equals(goal)) {
                        return true;
                    }
                    changed = true;
                }
            }
        }
        return false;
    }
}
