package org.example.booksys.scheduler;

import org.example.booksys.model.Booking;
import org.example.booksys.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    private BookingService bookingService;

    // This cron expression runs at 2:00 AM every day
    // The format is: second, minute, hour, day of month, month, day of week
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void issueNoShowWarnings() {
        System.out.println("Running scheduled task to issue NO_SHOW warnings.");

        // Find all confirmed bookings for days that have passed
        List<Booking> noShowCandidates = bookingService.getNoShowCandidates();

        for (Booking booking : noShowCandidates) {
            System.out.println("Issuing NO_SHOW warning for booking ID: " + booking.getId());
            bookingService.issueNoShowWarning(booking.getId());
        }
    }
}