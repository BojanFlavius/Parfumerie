package com.se.parfumerie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KnowledgeBasedSystem {
    private final Set<String> workingMemory;
    public final List<Rule> productionRules;

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
