package com.example.shareit;

import com.example.shareit.item.Item;
import com.example.shareit.item.ItemDTO;
import com.example.shareit.user.User;
import com.example.shareit.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public static UserDTO  toUserDto (User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUser (UserDTO userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }
    public static ItemDTO toItemDto (Item item) {
        return new ItemDTO (item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static Item toItem (ItemDTO itemDTO) {
        return Item.builder().id(itemDTO.getId()).name(itemDTO.getName()).description(itemDTO.getDescription())
                .available(itemDTO.getAvailable()).build();
    }
}