package com.jasper.schoolbell.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime startedAt;

    private LocalDateTime cancelledAt;

    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eventId")
    private Event event;

    @ToString.Exclude
    @OneToMany(mappedBy = "eventOccurrence")
    private List<Alert> alerts;

    @PostPersist
    private void postPersist() {
        createdAt = LocalDateTime.now();
    }
}
