package com.example.shareit.item;

import com.example.shareit.exceptions.WrongUserException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

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
        try {
            for (Field field : Item.class.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object val = field.get(item);
                    if (val != null) {
                        field.set(itemToUpdate, val);
                    }
                }
            }
//            System.out.println(obj1);
        } catch (IllegalAccessException e) {
            // Handle exception
        }
        return itemToUpdate;
    }

    @Override
    public Item getItem(long itemId) {
        return items.values().stream().flatMap(Collection::stream).filter(oneOfItems -> oneOfItems.getId().equals(itemId)).findFirst().get();
    }

    @Override
    public List<Item> getAllUserItems(long userId) {
        return  items.get(userId);
    }

    @Override
    public Map<Long, List<Item>> getAllItems() {
        return items;

    }

    @Override
    public List<Item> searchAvailableItems(String text) {
        return null;
    }
}

