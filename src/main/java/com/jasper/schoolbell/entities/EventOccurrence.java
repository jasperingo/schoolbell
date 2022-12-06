package com.jasper.schoolbell.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "event_occurrences")
public class EventOccurrence {
    public enum Venue {
        physical,
        virtual
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Venue venue;

    private String address;

    private String link;

    private int duration;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime cancelledAt;

    @Column(nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;

    @PostPersist
    private void postPersist() {
        createdAt = LocalDateTime.now();
    }
}
