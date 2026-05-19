package com.carla.contentinsighttool.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "theme_entry")
public class ThemeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String themeName;
    private int count;
    private LocalDateTime detectedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private CheckinBatch batch;

    public ThemeEntry() {}

    public ThemeEntry(String themeName, int count, LocalDateTime detectedAt, CheckinBatch batch) {
        this.themeName = themeName;
        this.count = count;
        this.detectedAt = detectedAt;
        this.batch = batch;
    }

    public Long getId() { return id; }
    public String getThemeName() { return themeName; }
    public int getCount() { return count; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public CheckinBatch getBatch() { return batch; }
}