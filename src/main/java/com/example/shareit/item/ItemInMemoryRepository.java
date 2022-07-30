package com.example.shareit.item;

import com.example.shareit.exceptions.WrongUserException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class ItemInMemoryRepository implements ItemRepository {

    @Getter
    private Map<Long, List<Item>> items;

    private long getId() {
        long lastId = items.values()
                .stream()
                .flatMap(Collection::stream)
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return ++lastId;
    }

    @Override
    public Item create(Item item) {
        item.setId(getId());
        items.compute(item.getOwnerId(), (ownerId, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item update(Item item, long userId, long itemId) {

        Item itemToUpdate = getItem(itemId);
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
        return itemToUpdate;
    }

    @Override
    public Item getItem(long itemId) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(oneOfItems -> oneOfItems.getId().equals(itemId))
                .findFirst().get();
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        return items.get(userId);
    }

    @Override
    public Map<Long, List<Item>> getAllItems() {
        return items;

    }

}

