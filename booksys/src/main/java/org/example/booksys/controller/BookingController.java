package org.example.booksys.controller;

import org.example.booksys.dto.BookingRequestDto;
import org.example.booksys.model.Booking;
import org.example.booksys.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    //@PreAuthorize("hasAnyRole('admins', 'staff')")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto bookingRequest) {
        Optional<Booking> newBooking = bookingService.createBooking(bookingRequest);
        if (newBooking.isPresent()) {
            return new ResponseEntity<>(newBooking.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to create booking. User, space, or time slot not available.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    //@PreAuthorize("hasRole('admins')")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('admins', 'staff')")
    public ResponseEntity<Booking> getBookingById(@PathVariable int id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('admins')")
    public ResponseEntity<?> updateBooking(@PathVariable int id, @RequestBody BookingRequestDto bookingRequest) {
        Optional<Booking> updatedBooking = bookingService.updateBooking(id, bookingRequest);
        if (updatedBooking.isPresent()) {
            return new ResponseEntity<>(updatedBooking.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to update booking. Booking ID, User, or Space not found.", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('admins')")
    public ResponseEntity<Void> deleteBooking(@PathVariable int id) {
        if (bookingService.deleteBooking(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/cancel")
    //@PreAuthorize("hasAnyRole('admins', 'staff')")
    public ResponseEntity<Void> cancelBooking(@PathVariable int id) {
        boolean isCancelled = bookingService.cancelBooking(id);
        if (isCancelled) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/checked-in")
    //@PreAuthorize("hasRole('admins')")
    public ResponseEntity<List<Booking>> getCheckedInBookings() {
        List<Booking> checkedInBookings = bookingService.getCheckedInBookings();
        return new ResponseEntity<>(checkedInBookings, HttpStatus.OK);
    }

    @GetMapping("/confirmed")
    //@PreAuthorize("hasRole('admins')")
    public ResponseEntity<List<Booking>> getConfirmedBookings() {
        List<Booking> confirmedBookings = bookingService.getConfirmedBookings();
        return new ResponseEntity<>(confirmedBookings, HttpStatus.OK);
    }

    @GetMapping("/cancelled")
    //@PreAuthorize("hasRole('admins')")
    public ResponseEntity<List<Booking>> getCancelledBookings() {
        List<Booking> cancelledBookings = bookingService.getCancelledBookings();
        return new ResponseEntity<>(cancelledBookings, HttpStatus.OK);
    }

    @GetMapping("/no-show")
    //@PreAuthorize("hasRole('admins')")
    public ResponseEntity<List<Booking>> getNoShowBookings() {
        List<Booking> noShowBookings = bookingService.getNoShowBookings();
        return new ResponseEntity<>(noShowBookings, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    //@PreAuthorize("hasAnyRole('admins', 'staff')")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable int userId) {
        List<Booking> userBookings = bookingService.getBookingsByUserId(userId);
        return new ResponseEntity<>(userBookings, HttpStatus.OK);
    }
}