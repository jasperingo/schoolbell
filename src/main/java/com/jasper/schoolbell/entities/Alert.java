package com.jasper.schoolbell.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alerts")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String callSessionId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String message;

    private boolean isActive;

    private Long duration;

    private Double cost;

    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "participantId")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "eventOccurrenceId")
    private EventOccurrence eventOccurrence;
}
