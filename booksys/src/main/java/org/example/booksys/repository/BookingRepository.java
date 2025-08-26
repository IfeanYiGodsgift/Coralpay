package org.example.booksys.repository;

import org.example.booksys.model.Booking;
import org.example.booksys.model.Space;
import org.example.booksys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Optional<Booking> findBySpaceAndBookingDateAndStartTime(Space space, LocalDate bookingDate, LocalTime startTime);
    List<Booking> findByStatus(Booking.BookingStatus status);
    List<Booking> findByStatusAndBookingDateAfter(Booking.BookingStatus status, LocalDate date);
    List<Booking> findByStatusAndBookingDateBefore(Booking.BookingStatus status, LocalDate date);
    List<Booking> findByUser(User user);

}