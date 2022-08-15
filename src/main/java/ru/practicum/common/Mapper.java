package ru.practicum.common;

import lombok.AllArgsConstructor;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingDTO;
import ru.practicum.booking.BookingJpaRepository;
import ru.practicum.booking.Status;
import ru.practicum.exceptions.NotExistedItemException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemDTO;
import ru.practicum.item.ItemJpaRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserDTO;
import org.springframework.stereotype.Component;
import ru.practicum.user.UserJpaRepository;

import javax.persistence.EntityNotFoundException;

@Component
@AllArgsConstructor
public class Mapper {

    private final BookingJpaRepository bookingRepository;
    private final ItemJpaRepository itemRepository;
    private final UserJpaRepository userRepository;

    public static UserDTO toUserDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDTO userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static ItemDTO toItemDto(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemDTO itemDTO) {
        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .available(itemDTO.getAvailable())
                .build();
    }

    public Booking toBooking(BookingDTO bookingDTO) {
        Item item;
              try {
                  item = itemRepository.getById(bookingDTO.getItemId());
              } catch (EntityNotFoundException e) {
                  throw new NotExistedItemException();
              }



        return Booking.builder()
                .start(bookingDTO.getStart())
                .end(bookingDTO.getEnd())
                .bookedItem(item)
                .booker()
                .status(Status.WAITING)
                .build();
    }

}