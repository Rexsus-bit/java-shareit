package com.example.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Booking {
    long bookingId;
    LocalDate startDate;
    LocalDate finishDate;
    long itemId;
    long bookerId;
    Status status;
}
