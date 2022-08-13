package ru.practicum.item;

import ru.practicum.exceptions.NotExistedUserException;
import ru.practicum.exceptions.WrongUserException;
import ru.practicum.user.User;
import ru.practicum.user.UserInMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.user.UserJPARepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemJpaRepository itemRepository;
    private final UserJPARepository userRepository;

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

    public Item getItem(long itemId) {
        return itemRepository.getById(itemId);
    }

    public List<Item> getAllUserItems(long userId) {
        return itemRepository.findAll().stream().filter(a-> a.getOwnerId() == userId).collect(Collectors.toList());
    }

    public List<Item> searchAvailableItems(String text) {
//        List<Item> itemList = itemRepository.findAll();
//        return itemList.stream().filter(a -> {
//            List<String> queryWords = Arrays.stream(text.split("\\s"))
//                    .map(String::toLowerCase)
//                    .collect(Collectors.toList());
//            if (text.isBlank()) return false;
//            return checkQuery(a, queryWords);
//        }).collect(Collectors.toList());
        return itemRepository.searchAvailableItems(text);
    }

    private boolean checkQuery(Item a, List<String> queryWords) {
        for (String queryWord : queryWords) {
            return (a.getName().toLowerCase().contains(queryWord) || a.getDescription().toLowerCase().contains(queryWord)) && a.getAvailable();
        }
        return false;
    }
}
