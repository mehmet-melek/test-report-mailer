package com.melek.model;

import java.util.List;

public class Feature {

    private String name; // description'dan gelecek.
    private List<Scenario> scenarios;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
    }
}
