package com.carla.contentinsighttool.dto;

public class TrendingThemeDto {

    private String theme;
    private long totalCount;

    public TrendingThemeDto(String theme, long totalCount) {
        this.theme = theme;
        this.totalCount = totalCount;
    }

    public String getTheme() { return theme; }
    public long getTotalCount() { return totalCount; }
}