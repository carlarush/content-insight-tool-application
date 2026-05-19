package com.carla.contentinsighttool.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "content_idea")
public class ContentIdea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String ideaText;

    private String ideaType;
    private LocalDateTime generatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private CheckinBatch batch;

    public ContentIdea() {}

    public ContentIdea(String ideaText, String ideaType, LocalDateTime generatedAt, CheckinBatch batch) {
        this.ideaText = ideaText;
        this.ideaType = ideaType;
        this.generatedAt = generatedAt;
        this.batch = batch;
    }

    public Long getId() { return id; }
    public String getIdeaText() { return ideaText; }
    public String getIdeaType() { return ideaType; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public CheckinBatch getBatch() { return batch; }
}