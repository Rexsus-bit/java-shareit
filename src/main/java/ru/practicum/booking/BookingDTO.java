package ru.practicum.booking;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
public class BookingDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long ItemId;
    private Long bookerId;
    private Status status;

    public BookingDTO() {
    }


}
