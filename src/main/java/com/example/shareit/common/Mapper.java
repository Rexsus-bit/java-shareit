package com.example.shareit.common;

import com.example.shareit.item.Item;
import com.example.shareit.item.ItemDTO;
import com.example.shareit.user.User;
import com.example.shareit.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public static UserDTO  toUserDto (User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser (UserDTO userDto) {
        return User.builder()
                .id(userDto.getId())
                .name( userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
    public static ItemDTO toItemDto (Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem (ItemDTO itemDTO) {
        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .available(itemDTO.getAvailable())
                .build();
    }
}