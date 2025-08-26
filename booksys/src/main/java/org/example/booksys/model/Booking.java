package org.example.booksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Bookings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"space_id", "booking_date", "start_time"})
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('CONFIRMED', 'CANCELLED', 'CHECKED_IN', 'NO_SHOW') NOT NULL DEFAULT 'CONFIRMED'")
    private BookingStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum BookingStatus {
        CONFIRMED,
        CANCELLED,
        CHECKED_IN,
        NO_SHOW
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = BookingStatus.CONFIRMED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}