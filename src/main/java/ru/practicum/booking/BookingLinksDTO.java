package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingLinksDTO {
    private Long id;
    private Long bookerId;
}
