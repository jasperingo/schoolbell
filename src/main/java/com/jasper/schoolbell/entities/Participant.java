package com.jasper.schoolbell.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean host;

    @Column(nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "eventId")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @PostPersist
    private void postPersist() {
        createdAt = LocalDateTime.now();
    }
}
