package org.example.booksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String msEmail;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('STAFF', 'ADMIN') NOT NULL DEFAULT 'STAFF'")
    private Role role = Role.STAFF;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(nullable = false)
    private Timestamp updatedAt;

    public User(String msEmail, String firstName, String lastName, Role role) {
        this.msEmail = msEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(Instant.now());
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public enum Role {
        STAFF,
        ADMIN
    }
}