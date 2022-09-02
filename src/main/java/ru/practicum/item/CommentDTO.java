package ru.practicum.item;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;



}
