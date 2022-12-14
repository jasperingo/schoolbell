package com.jasper.schoolbell.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "event")
    private List<Participant> participants;

    @ToString.Exclude
    @OneToMany(mappedBy = "event")
    private List<EventOccurrence> eventOccurrences;

    @PostPersist
    private void postPersist() {
        createdAt = LocalDateTime.now();
    }
}
