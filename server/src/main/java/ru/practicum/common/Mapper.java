package ru.practicum.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingDTO;
import ru.practicum.booking.Status;
import ru.practicum.exceptions.NotExistedItemException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.UnavailableItemException;
import ru.practicum.item.*;
import ru.practicum.request.ItemRequest;
import ru.practicum.request.ItemRequestDTO;
import ru.practicum.user.User;
import ru.practicum.user.UserDTO;
import ru.practicum.user.UserJpaRepository;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class Mapper {

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

    public ItemDTO toItemDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    public static Item toItem(ItemDTO itemDTO) {
        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .available(itemDTO.getAvailable())
                .requestId(itemDTO.getRequestId())
                .build();
    }

    public Booking toBooking(BookingDTO bookingDTO) {
        Item item;
        User booker;

        if (!itemRepository.existsById(bookingDTO.getItemId())) throw new NotExistedItemException();
        item = itemRepository.getById(bookingDTO.getItemId());

        if (!userRepository.existsById(bookingDTO.getBookerId())) throw new NotExistedUserException();
        booker = userRepository.getById(bookingDTO.getBookerId());

        if (!item.getAvailable()) {
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

    public CommentDTO toCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(LocalDateTime.now())
                .build();
    }

    public ItemRequest toItemRequest(ItemRequestDTO itemRequestDTO) {
        return ItemRequest.builder()
                .id(itemRequestDTO.getId())
                .description(itemRequestDTO.getDescription())
                .requesterId(itemRequestDTO.getRequesterId())
                .created(itemRequestDTO.getCreated())
                .build();
    }

    public ItemRequestDTO toItemRequestDTO(ItemRequest itemRequest) {
        return ItemRequestDTO.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requesterId(itemRequest.getRequesterId())
                .created(itemRequest.getCreated())
                .items(itemRepository.findAllByRequestId(itemRequest.getId()))
                .build();
    }


}