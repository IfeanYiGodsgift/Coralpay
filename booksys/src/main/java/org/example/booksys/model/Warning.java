package org.example.booksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Warnings")
public class Warning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = true)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WarningType warningType;

    private String reason;

    @Column(nullable = false)
    private LocalDateTime warningDate;

    public enum WarningType {
        UNBOOKED_ENTRY,
        NO_SHOW
    }

    @PrePersist
    protected void onCreate() {
        this.warningDate = LocalDateTime.now();
    }
}