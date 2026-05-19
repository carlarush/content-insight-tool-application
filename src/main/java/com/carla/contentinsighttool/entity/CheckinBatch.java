package com.carla.contentinsighttool.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "checkin_batch")
public class CheckinBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "checkin_text", joinColumns = @JoinColumn(name = "batch_id"))
    @Column(name = "text", columnDefinition = "TEXT")
    private List<String> checkins;

    private LocalDateTime submittedAt;

    public CheckinBatch() {}

    public CheckinBatch(List<String> checkins, LocalDateTime submittedAt) {
        this.checkins = checkins;
        this.submittedAt = submittedAt;
    }

    public Long getId() { return id; }
    public List<String> getCheckins() { return checkins; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
}