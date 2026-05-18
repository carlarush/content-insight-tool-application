package com.carla.contentinsighttool.model;

import java.util.List;
import java.util.Map;

public class ContentResponse {

    private String summary;
    private List<ThemeResult> themes;
    private Map<String, Object> aiIdeas;

    public ContentResponse() {
    }

    public ContentResponse(String summary, List<ThemeResult> themes, Map<String, Object> aiIdeas) {
        this.summary = summary;
        this.themes = themes;
        this.aiIdeas = aiIdeas;
    }

    public String getSummary() {
        return summary;
    }

    public List<ThemeResult> getThemes() {
        return themes;
    }

    public Map<String, Object> getAiIdeas() {
        return aiIdeas;
    }
}