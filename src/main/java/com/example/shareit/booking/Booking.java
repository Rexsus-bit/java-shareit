package com.example.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Booking {
    private long bookingId;
    private LocalDate startDate;
    private LocalDate finishDate;
    private long itemId;
    private long bookerId;
    private Status status;
}
