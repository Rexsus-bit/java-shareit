package com.example.shareit.item;

import com.example.shareit.Mapper;
import com.example.shareit.exceptions.NotExcistedUserException;
import com.example.shareit.user.UserInMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {


    private final Mapper mapper;
    private final ItemRepository itemRepository;
    private final UserInMemoryRepository userRepository;

    public ItemDTO create(Item item, long userId) {
        item.setOwnerId(userId);
        itemValidation(item);

        itemRepository.create(item);
        return mapper.toItemDto(item);
    }

    private void itemValidation(Item item) {
        if ( !userRepository.getUsers().containsKey(item.getOwnerId()))
        throw new NotExcistedUserException();
    }

    public ItemDTO update(Item item, long userId, long itemId) {
        return  mapper.toItemDto(itemRepository.update(item, userId, itemId));

    }

    public ItemDTO getItem(long itemId) {
        return mapper.toItemDto(itemRepository.getItem(itemId));
    }

    public List<ItemDTO> getAllUserItems(long userId) {
        return itemRepository.getAllUserItems(userId).stream().map(Mapper::toItemDto).collect(Collectors.toList());
    }

    public List<Item> searchAvailableItems(String text) {
        Map<Long, List<Item>> itemsMap = itemRepository.getAllItems();
        List<Item> itemList = itemsMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        return itemList.stream().filter(a -> {
            List<String> queryWords = Arrays.stream(text.split("\\s")).map(String::toLowerCase).collect(Collectors.toList());
            if (text.isBlank()) return false;
            for (String queryWord : queryWords) {
                return (a.getName().toLowerCase().contains(queryWord) || a.getDescription().toLowerCase().contains(queryWord)) && a.getAvailable();
            }
            return false;
        }).collect(Collectors.toList());
    }
}
