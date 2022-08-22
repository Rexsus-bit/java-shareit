package ru.practicum.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.item.Comment;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
public class BookingDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private boolean available;

    public BookingDTO() {
    }

    public BookingDTO(Long id, LocalDateTime start, LocalDateTime end, Long itemId, Long bookerId, Boolean available) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.available = available;
    }
}
