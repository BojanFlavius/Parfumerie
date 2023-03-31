package com.se.parfumerie;

import java.util.List;

public class Rule {
    private final String condition;
    private final String conclusion;

    public Rule(String condition, String conclusion) {
        this.condition = condition;
        this.conclusion = conclusion;
    }

    public boolean applies(List<String> workingMemory) {
        return workingMemory.contains(condition) && !workingMemory.contains(conclusion);
    }

    public String getConclusion() {
        return conclusion;
    }
}
