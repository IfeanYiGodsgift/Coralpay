package org.example.booksys.controller;

import org.example.booksys.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/{bookingId}")
    public ResponseEntity<Void> checkIn(@PathVariable int bookingId) {
        boolean success = bookingService.checkIn(bookingId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/unbooked/{userId}")
    public ResponseEntity<Void> unbookedCheckinAttempt(@PathVariable int userId) {
        bookingService.issueUnbookedEntryWarning(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}