package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Booking {
    private long id;
    private LocalDateTime start;
    private LocalDateTime finish;
    private long itemId;
    private long bookerId;
    private Status status;
}
