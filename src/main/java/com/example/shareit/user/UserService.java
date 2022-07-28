package com.example.shareit.user;

import com.example.shareit.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Mapper mapper;
    private final UserInMemoryRepository userRepository;

    public UserDTO create(User user) {
        userValidation(user);
        userRepository.create(user);
        return mapper.toUserDto(user);
    }

    private void userValidation(User user) {
        userRepository.getUsers().values().forEach(
                user1 -> { if (user1.getEmail().equals(user.getEmail()))
                    throw new ValidationException();}
        );
    }

    public UserDTO update(User user, long userId) {
        userValidation(user);
        return mapper.toUserDto(userRepository.update(user, userId));
    }

    public UserDTO get(long userId) {
        return mapper.toUserDto(userRepository.get(userId));
    }

    public List<UserDTO> getAll() {
        return userRepository.getAll().stream().map(Mapper::toUserDto).collect(Collectors.toList());
    }
    public UserDTO delete(long userId) {
        return mapper.toUserDto(userRepository.delete(userId));
    }


//
//    public ItemDTO update(Item item, long userId, long itemId) {
//        return  mapper.toItemDto(userRepository.update(item, userId, itemId));
//
//
//
//
//    }
//
//    public ItemDTO getItem(long itemId) {
//        return mapper.toItemDto(userRepository.getItem(itemId));
//    }
//
//    public void getUserItems(long userId) {
//    }
//
//    public List<Item> searchAvailableItems(String text) {
//        return null;
//    }
//}


}
