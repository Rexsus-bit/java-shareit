package ru.practicum.item;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class CommentDTO {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;

}
