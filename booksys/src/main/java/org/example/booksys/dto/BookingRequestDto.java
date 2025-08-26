package org.example.booksys.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequestDto {
    private int userId;
    private int spaceId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
}