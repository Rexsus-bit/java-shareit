package com.example.shareit.item;

import com.example.shareit.common.Mapper;
import com.example.shareit.exceptions.NotExistedUserException;
import com.example.shareit.user.UserInMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {


    private final Mapper mapper;
    private final ItemRepository itemRepository;
    private final UserInMemoryRepository userRepository;

    public Item create(Item item, long userId) {
        item.setOwnerId(userId);
        itemValidation(item);
        itemRepository.create(item);
        return item;
    }

    private void itemValidation(Item item) {
        if (!userRepository.getUsers().containsKey(item.getOwnerId())) {
            throw new NotExistedUserException();
        }
    }

    public Item update(Item item, long userId, long itemId) {
        return itemRepository.update(item, userId, itemId);
    }

    public Item getItem(long itemId) {
        return itemRepository.getItem(itemId);
    }

    public List<Item> getAllUserItems(long userId) {
        return new ArrayList<>(itemRepository.getAllUserItems(userId));
    }

    public List<Item> searchAvailableItems(String text) {
        Map<Long, List<Item>> itemsMap = itemRepository.getAllItems();
        List<Item> itemList = itemsMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return itemList.stream().filter(a -> {
            List<String> queryWords = Arrays.stream(text.split("\\s"))
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            if (text.isBlank()) return false;
            return checkQuery(a, queryWords);
        }).collect(Collectors.toList());
    }

    private boolean checkQuery(Item a, List<String> queryWords) {
        for (String queryWord : queryWords) {
            return (a.getName().toLowerCase().contains(queryWord) || a.getDescription().toLowerCase().contains(queryWord)) && a.getAvailable();
        }
        return false;
    }
}
