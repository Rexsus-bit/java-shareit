package com.example.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ItemDTO {
    long id;
    String name;
    String description;
    boolean available;
}
