package org.example.booksys.service;

import org.example.booksys.dto.BookingRequestDto;
import org.example.booksys.model.Booking;
import org.example.booksys.model.Space;
import org.example.booksys.model.User;
import org.example.booksys.model.Warning;
import org.example.booksys.repository.BookingRepository;
import org.example.booksys.repository.SpaceRepository;
import org.example.booksys.repository.UserRepository;
import org.example.booksys.repository.WarningRepository;
import org.example.booksys.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private WarningRepository warningRepository;

    @Autowired
    private EmailService emailService;

    public Optional<Booking> createBooking(BookingRequestDto bookingRequest) {
        // 1. Validate if user and space exist
        Optional<User> userOptional = userRepository.findById(bookingRequest.getUserId());
        Optional<Space> spaceOptional = spaceRepository.findById(bookingRequest.getSpaceId());

        if (userOptional.isEmpty() || spaceOptional.isEmpty()) {
            return Optional.empty(); // User or Space not found
        }

        User user = userOptional.get();
        Space space = spaceOptional.get();

        // 2. Check for existing bookings for the requested space and time
        boolean isSpaceAvailable = bookingRepository.findBySpaceAndBookingDateAndStartTime(
                space,
                bookingRequest.getBookingDate(),
                bookingRequest.getStartTime()
        ).isEmpty();

        if (!isSpaceAvailable) {
            return Optional.empty(); // Space is not available at this time
        }

        // 3. Create and save the new booking
        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setSpace(space);
        newBooking.setBookingDate(bookingRequest.getBookingDate());
        newBooking.setStartTime(bookingRequest.getStartTime());
        newBooking.setEndTime(bookingRequest.getEndTime());

        Optional<Booking> savedBooking = Optional.of(bookingRepository.save(newBooking));

        if (savedBooking.isPresent()) {
            // Send confirmation email
            String subject = "Booking Confirmation: " + space.getName();
            String body = "Hi " + user.getFirstName() + ",\n\n" +
                    "Your booking for " + space.getName() + " on " + newBooking.getBookingDate() +
                    " from " + newBooking.getStartTime() + " to " + newBooking.getEndTime() +
                    " has been confirmed.";
            emailService.sendEmail(user.getMsEmail(), subject, body);
        }

        return savedBooking;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(int id) {
        return bookingRepository.findById(id);
    }

    public Optional<Booking> updateBooking(int id, BookingRequestDto updatedBookingDto) {
        return bookingRepository.findById(id).flatMap(existingBooking -> {
            Optional<User> userOptional = userRepository.findById(updatedBookingDto.getUserId());
            Optional<Space> spaceOptional = spaceRepository.findById(updatedBookingDto.getSpaceId());

            if (userOptional.isPresent() && spaceOptional.isPresent()) {
                // Ensure the updated booking doesn't conflict with other bookings for the same space
                Optional<Booking> conflictBooking = bookingRepository.findBySpaceAndBookingDateAndStartTime(
                        spaceOptional.get(),
                        updatedBookingDto.getBookingDate(),
                        updatedBookingDto.getStartTime());

                if (conflictBooking.isPresent() && conflictBooking.get().getId() != existingBooking.getId()) {
                    // There's a conflict with another booking
                    return Optional.empty();
                }

                existingBooking.setUser(userOptional.get());
                existingBooking.setSpace(spaceOptional.get());
                existingBooking.setBookingDate(updatedBookingDto.getBookingDate());
                existingBooking.setStartTime(updatedBookingDto.getStartTime());
                existingBooking.setEndTime(updatedBookingDto.getEndTime());

                return Optional.of(bookingRepository.save(existingBooking));
            } else {
                return Optional.empty();
            }
        });
    }

    public boolean deleteBooking(int id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean cancelBooking(int bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            // Get the start of the booked day and subtract 24 hours
            LocalDateTime cancellationCutoff = booking.getBookingDate().atStartOfDay().minusHours(24);

            // Check if the current time is before the 24-hour cutoff
            if (LocalDateTime.now().isBefore(cancellationCutoff)) {
                booking.setStatus(Booking.BookingStatus.CANCELLED);
                bookingRepository.save(booking);

                // Send cancellation email
                String subject = "Booking Cancellation: " + booking.getSpace().getName();
                String body = "Hi " + booking.getUser().getFirstName() + ",\n\n" +
                        "Your booking for " + booking.getSpace().getName() + " on " + booking.getBookingDate() +
                        " has been successfully cancelled.";
                emailService.sendEmail(booking.getUser().getMsEmail(), subject, body);

                return true;
            }
        }
        return false;
    }

    public boolean checkIn(int bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            if (booking.getStatus() == Booking.BookingStatus.CONFIRMED) {
                booking.setStatus(Booking.BookingStatus.CHECKED_IN);
                bookingRepository.save(booking);
                return true;
            }
        }
        return false;
    }

    public List<Booking> getCheckedInBookings() {
        return bookingRepository.findByStatus(Booking.BookingStatus.CHECKED_IN);
    }

    public List<Booking> getConfirmedBookings() {
        return bookingRepository.findByStatus(Booking.BookingStatus.CONFIRMED);
    }

    public List<Booking> getCancelledBookings() {
        return bookingRepository.findByStatus(Booking.BookingStatus.CANCELLED);
    }

    public List<Booking> getNoShowBookings() {
        return bookingRepository.findByStatus(Booking.BookingStatus.NO_SHOW);
    }

    public List<Booking> getNoShowCandidates() {
        return bookingRepository.findByStatusAndBookingDateBefore(Booking.BookingStatus.CONFIRMED, LocalDate.now());
    }

    @Transactional
    public void issueNoShowWarning(int bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            Warning warning = new Warning();
            warning.setUser(booking.getUser());
            warning.setBooking(booking);
            warning.setWarningType(Warning.WarningType.NO_SHOW);
            warning.setReason("Booking not checked in by end time.");
            warningRepository.save(warning);
            booking.setStatus(Booking.BookingStatus.NO_SHOW);
            bookingRepository.save(booking);

            // Send NO_SHOW warning email
            String subject = "NO-SHOW Warning: Booking Not Used";
            String body = "Hi " + booking.getUser().getFirstName() + ",\n\n" +
                    "This is an automated warning to inform you that your booking for " +
                    booking.getSpace().getName() + " on " + booking.getBookingDate() +
                    " was marked as a 'NO-SHOW'.";
            emailService.sendEmail(booking.getUser().getMsEmail(), subject, body);
        }
    }

    @Transactional
    public void issueUnbookedEntryWarning(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Warning warning = new Warning();
            warning.setUser(user);
            warning.setWarningType(Warning.WarningType.UNBOOKED_ENTRY);
            warning.setReason("Failed check-in attempt with no valid booking.");
            warningRepository.save(warning);

            // Send UNBOOKED_ENTRY warning email
            String subject = "Unbooked Entry Warning";
            String body = "Hi " + user.getFirstName() + ",\n\n" +
                    "You have received a warning for an unbooked check-in attempt.";
            emailService.sendEmail(user.getMsEmail(), subject, body);
        }
    }

    public List<Booking> getBookingsByUserId(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return bookingRepository.findByUser(userOptional.get());
        }
        return List.of();
    }
}