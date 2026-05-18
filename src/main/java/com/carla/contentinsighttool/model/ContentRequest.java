package com.carla.contentinsighttool.model;

import java.util.List;

public class ContentRequest {

    private List<String> checkins;

    public ContentRequest() {
    }

    public List<String> getCheckins() {
        return checkins;
    }

    public void setCheckins(List<String> checkins) {
        this.checkins = checkins;
    }
}