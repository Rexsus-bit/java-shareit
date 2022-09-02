package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingJpaRepository;
import ru.practicum.booking.BookingLinksDTO;
import ru.practicum.booking.Status;
import ru.practicum.common.Mapper;
import ru.practicum.exceptions.NotExistedItemException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.ValidationException;
import ru.practicum.exceptions.WrongUserException;
import ru.practicum.user.UserJpaRepository;
import ru.practicum.util.OffsetLimitPageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemJpaRepository itemRepository;

    private final BookingJpaRepository bookingRepository;
    private final UserJpaRepository userRepository;
    private final CommentJpaRepository commentRepository;
    private final Mapper mapper;

    public Item create(Item item, long userId) {
        item.setOwnerId(userId);
        itemValidation(item);
        return itemRepository.save(item);
    }

    private void itemValidation(Item item) {
        if (userRepository.findAll().stream().noneMatch(a -> a.getId().equals(item.getOwnerId()))) {
            throw new NotExistedUserException();
        }
    }

    public Item update(Item item, long userId, long itemId) {
        Item itemToUpdate = itemRepository.getById(itemId);
        long ownerId = itemToUpdate.getOwnerId();
        if (ownerId != userId) throw new WrongUserException();
        if (item.getName() != null) {
            itemToUpdate.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemToUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemToUpdate.setAvailable(item.getAvailable());
        }
        return itemRepository.save(itemToUpdate);
    }

    public ItemDTO getItem(long itemId, long userId) {
        LocalDateTime currentTime = LocalDateTime.now();
        Item item = itemRepository.findById(itemId).orElseThrow(NotExistedItemException::new);
        ItemDTO itemDTO = mapper.toItemDTO(item);

        if (userId == item.getOwnerId()) {
            List<Booking> bookingsInPast = bookingRepository
                    .findAllByItemIdAndEndIsBeforeOrderByEndDesc(itemId, currentTime);

            List<Booking> bookingsInFuture = bookingRepository
                    .findAllByItemIdAndStartIsAfterOrderByStartAsc(itemId, currentTime);
            if (bookingsInPast.size() > 0)
                itemDTO.setLastBooking(new BookingLinksDTO(bookingsInPast.get(0).getId(),
                        bookingsInPast.get(0).getBooker().getId()));
            if (bookingsInFuture.size() > 0)
                itemDTO.setNextBooking(new BookingLinksDTO(bookingsInFuture.get(0).getId(),
                        bookingsInFuture.get(0).getBooker().getId()));
        }
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        itemDTO.setComments(new HashSet<>(comments.stream().map(mapper::toCommentDTO)
                .collect(Collectors.toList())));
        return itemDTO;
    }

    public List<ItemDTO> getAllUserItems(long userId, Integer from, Integer size) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<ItemDTO> itemsDTO = itemRepository.findAll().stream().filter(a -> a.getOwnerId() == userId)
                .map(mapper::toItemDTO).collect(Collectors.toList());
        itemsDTO.forEach((itemDTO) -> {
            Long itemId = itemDTO.getId();
            Pageable page = OffsetLimitPageable.of(from, size, Sort.by(Sort.Direction.DESC, "end"));
            Page<Booking> bookingsInPastPage = bookingRepository
                    .findAllByItemIdAndEndIsBeforeOrderByEndDesc(itemId, currentTime, page);
            List<Booking> bookingsInPast = bookingsInPastPage.getContent();
            page = OffsetLimitPageable.of(from, size, Sort.by(Sort.Direction.ASC, "start"));

            Page<Booking> bookingsInFuturePage = bookingRepository
                    .findAllByItemIdAndStartIsAfterOrderByStartAsc(itemId, currentTime, page);
            List<Booking> bookingsInFuture = bookingsInFuturePage.getContent();
            if (bookingsInPast.size() > 0) {
                BookingLinksDTO lastBooking = new BookingLinksDTO(bookingsInPast.get(0).getId(),
                        bookingsInPast.get(0).getBooker().getId());
                itemDTO.setLastBooking(lastBooking);
            }
            if (bookingsInFuture.size() > 0) {
                BookingLinksDTO nextBookings = new BookingLinksDTO(bookingsInFuture.get(0).getId(),
                        bookingsInFuture.get(0).getBooker().getId());
                itemDTO.setNextBooking(nextBookings);
            }
        });
        return itemsDTO.stream()
                .sorted(Comparator.comparingLong(ItemDTO::getId))
                .peek(
                (itemDTO) -> {
                    if (itemDTO.getComments() == null) itemDTO.setComments(new HashSet<>());
                })
                .collect(Collectors.toList());
    }

    public List<Item> searchAvailableItems(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return itemRepository.searchAvailableItems(text);
    }

    public CommentDTO addComment(long userId, Comment comment, long itemId) {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemIdAndStatusOrderByStartDesc(userId,
                itemId, Status.APPROVED);
        if (bookings.size() == 0) throw new ValidationException();
        boolean hasBeenAlreadyRented = bookings.stream().anyMatch(a -> a.getEnd().isBefore(
                LocalDateTime.now()));
        if (comment.getText().isBlank() || !hasBeenAlreadyRented) throw new ValidationException();
        comment.setAuthor(userRepository.findById(userId).orElseThrow(NotExistedUserException::new));
        comment.setItem(itemRepository.findById(itemId).orElseThrow(NotExistedItemException::new));
        return mapper.toCommentDTO(commentRepository.save(comment));
    }
}
