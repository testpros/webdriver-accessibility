package com.accessibility;

import java.util.List;

public class Result {
    private String rule;
    private List<String> elements;
    private String informationLink;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public String getUrl() {
        return informationLink;
    }

    public void setUrl(String url) {
        this.informationLink = url;
    }

}
