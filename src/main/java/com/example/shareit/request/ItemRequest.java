package com.example.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemRequest {
    long id;
    String description;
    long requestorId;
    LocalDateTime created;
}
