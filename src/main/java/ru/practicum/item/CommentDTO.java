package ru.practicum.item;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentDTO {
    private Long id;
    private String text;
    private String authorName;
    private Boolean created;

}
