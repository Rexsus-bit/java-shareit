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

    public static ItemDTO toItemDto (Item item) {

        return new ItemDTO (item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

}
