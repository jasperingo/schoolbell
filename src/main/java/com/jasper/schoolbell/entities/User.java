package com.jasper.schoolbell.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserPrincipal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Participant> participants;

    @PostPersist
    private void postPersist() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public String getName() {
        return phoneNumber;
    }
}
