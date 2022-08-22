package ru.practicum.common;

import lombok.AllArgsConstructor;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingDTO;
import ru.practicum.booking.BookingJpaRepository;
import ru.practicum.booking.Status;
import ru.practicum.exceptions.NotExistedItemException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.UnavailableItemException;
import ru.practicum.item.*;
import ru.practicum.user.User;
import ru.practicum.user.UserDTO;
import org.springframework.stereotype.Component;
import ru.practicum.user.UserJpaRepository;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class Mapper {

    private final ItemJpaRepository itemRepository;
    private final UserJpaRepository userRepository;
    private final CommentJpaRepository commentRepository;

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

    public ItemDTO toItemDto(Item item) {

//            List comments = commentRepository.findAll();
//        System.out.println(comments);
//        if () itemDTO.setComments(comments.stream().map(Optional::get).collect(Collectors.toSet()));


        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
//                .comments(comments)
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
        User booker;

        if (!itemRepository.existsById(bookingDTO.getItemId()))  throw new NotExistedItemException();
        item = itemRepository.getById(bookingDTO.getItemId());

        if (!userRepository.existsById(bookingDTO.getBookerId())) throw new NotExistedUserException();
        booker = userRepository.getById(bookingDTO.getBookerId());

        if (!item.getAvailable()){
            throw new UnavailableItemException();
        }

        return Booking.builder().id(null)
                .start(bookingDTO.getStart())
                .end(bookingDTO.getEnd())
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();
    }

    public BookingDTO toBookingDto(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }

}