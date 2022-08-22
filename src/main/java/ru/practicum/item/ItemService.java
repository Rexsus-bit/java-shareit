package ru.practicum.item;

import ru.practicum.booking.Booking;
import ru.practicum.booking.BookingJpaRepository;
import ru.practicum.booking.BookingLinksDTO;
import ru.practicum.common.Mapper;
import ru.practicum.exceptions.NotExistedBookingException;
import ru.practicum.exceptions.NotExistedItemException;
import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.WrongUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.user.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.*;
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
        if (userRepository.findAll().stream().noneMatch(a-> a.getId() == item.getOwnerId())) {
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
        LocalDateTime CURRENT_TIME = LocalDateTime.now();
        Item item = itemRepository.findById(itemId).orElseThrow(NotExistedItemException::new);
        ItemDTO itemDTO = mapper.toItemDto(item);

        if (userId == item.getOwnerId()){
            List<Booking> bookingsInPast = bookingRepository.
                    findAllByItemIdAndEndIsBeforeOrderByEndDesc(itemId,CURRENT_TIME);

            List<Booking> bookingsInFuture = bookingRepository.
                    findAllByItemIdAndStartIsAfterOrderByStartAsc(itemId,CURRENT_TIME);
            if (bookingsInPast.size() > 0) itemDTO.setLastBooking(new BookingLinksDTO(bookingsInPast.get(0).getId(),bookingsInPast.get(0).getBooker().getId()));
            if (bookingsInFuture.size() > 0) itemDTO.setNextBooking(new BookingLinksDTO(bookingsInFuture.get(0).getId(),bookingsInFuture.get(0).getBooker().getId()));
        }

        List<Comment> s = commentRepository.findAll();

        return itemDTO;
    }

    public List<ItemDTO> getAllUserItems(long userId) {
        LocalDateTime CURRENT_TIME = LocalDateTime.now();
        List<ItemDTO> s = itemRepository.findAll().stream().filter(a-> a.getOwnerId() == userId).map(mapper::toItemDto).collect(Collectors.toList());

        s.forEach((a) -> {
            Long itemId = a.getId();
            List<Booking> bookingsInPast = bookingRepository.
                    findAllByItemIdAndEndIsBeforeOrderByEndDesc(itemId,CURRENT_TIME);
            List<Booking> bookingsInFuture = bookingRepository.
                    findAllByItemIdAndStartIsAfterOrderByStartAsc(itemId,CURRENT_TIME);
            if (bookingsInPast.size() > 0) a.setLastBooking(new BookingLinksDTO(bookingsInPast.get(0).getId(),bookingsInPast.get(0).getBooker().getId()));
            if (bookingsInFuture.size() > 0) a.setNextBooking(new BookingLinksDTO(bookingsInFuture.get(0).getId(),bookingsInFuture.get(0).getBooker().getId()));
        });
        return s.stream().sorted(Comparator.comparingLong(ItemDTO::getId)).collect(Collectors.toList());
    }

    public List<Item> searchAvailableItems(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return itemRepository.searchAvailableItems(text);
    }

    private boolean checkQuery(Item a, List<String> queryWords) {
        for (String queryWord : queryWords) {
            return (a.getName().toLowerCase().contains(queryWord) || a.getDescription().toLowerCase().contains(queryWord)) && a.getAvailable();
        }
        return false;
    }

    public Comment addComment(long userId, Comment comment, long itemId) {
        comment.setAuthor(userRepository.findById(userId).orElseThrow(NotExistedUserException::new));
        comment.setItem(itemRepository.findById(itemId).orElseThrow(NotExistedItemException::new));
        return commentRepository.save(comment);
    }
}
