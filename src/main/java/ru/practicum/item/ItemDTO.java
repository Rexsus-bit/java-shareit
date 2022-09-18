package ru.practicum.item;

import lombok.*;
import ru.practicum.booking.BookingLinksDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class ItemDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private BookingLinksDTO lastBooking;
    private BookingLinksDTO nextBooking;
    private Set<CommentDTO> comments;
    private Long requestId;
}
