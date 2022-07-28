package com.example.shareit.item;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ItemRepository {

    public Item create(Item item);

    Item update(Item item, long userId, long itemId);

    Item getItem(long itemId);

   Collection<Item> getAllUserItems(long userId);

    Map<Long, List<Item>> getAllItems();

    List<Item> searchAvailableItems(String text);
}
