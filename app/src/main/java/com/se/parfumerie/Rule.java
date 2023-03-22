package com.se.parfumerie;

import java.util.Map;

public class Rule {
    private final String condition;
    private final String conclusion;

    public Rule(String condition, String conclusion) {
        this.condition = condition;
        this.conclusion = conclusion;
    }

    public boolean applies(Map<String, Boolean> workingMemory) {
        return workingMemory.containsKey(condition) && workingMemory.get(condition);
    }

    public String getConclusion() {
        return conclusion;
    }
}
