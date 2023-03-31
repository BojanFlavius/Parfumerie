package com.se.parfumerie;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBasedSystem {
    public final List<String> workingMemory;
    private final List<Rule> productionRules;

    public KnowledgeBasedSystem() {
        this.workingMemory = new ArrayList<>();
        this.productionRules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        productionRules.add(rule);
    }

    public void addFact(String fact) {
        workingMemory.add(fact);
    }

    public void infer() {
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
        // aici avem workin memory complet si trebuie ales parfumul
        System.out.println(workingMemory);
    }
}
