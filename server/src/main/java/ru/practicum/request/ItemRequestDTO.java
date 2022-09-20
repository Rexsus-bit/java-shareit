package ru.practicum.request;

import lombok.*;
import ru.practicum.item.Item;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class ItemRequestDTO {

    private long id;
    @NotNull
    private String description;
    private long requesterId;
    private LocalDateTime created;
    List<Item> items;
}
