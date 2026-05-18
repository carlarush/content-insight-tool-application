package com.carla.contentinsighttool.model;

import java.util.List;

public class ThemeResult {

    private String name;
    private int count;
    private List<String> contentIdeas;
    private List<String> podcastIdeas;
    private List<String> resourceIdeas;

    public ThemeResult() {
    }

    public ThemeResult(String name, int count,
                       List<String> contentIdeas,
                       List<String> podcastIdeas,
                       List<String> resourceIdeas) {
        this.name = name;
        this.count = count;
        this.contentIdeas = contentIdeas;
        this.podcastIdeas = podcastIdeas;
        this.resourceIdeas = resourceIdeas;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public List<String> getContentIdeas() {
        return contentIdeas;
    }

    public List<String> getPodcastIdeas() {
        return podcastIdeas;
    }

    public List<String> getResourceIdeas() {
        return resourceIdeas;
    }
}