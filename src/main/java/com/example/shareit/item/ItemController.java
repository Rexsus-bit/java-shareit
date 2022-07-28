package com.example.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    ItemDTO create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @NonNull @Valid Item item) {
        return itemService.create(item, userId);
    }

    @PatchMapping("{itemId}")
    ItemDTO update(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @NonNull Item item, @PathVariable long itemId) {
        return itemService.update(item, userId, itemId);
    }

    @GetMapping("/{id}")
    ItemDTO getItem(@PathVariable("id") long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    List<ItemDTO> getAllUserItem(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllUserItems(userId);
    }

//    @DeleteMapping //TODO ADDITIONAL
//    Item delete() {
//        itemService.getDelleteUser(itemId, userId);
//        return null;
//    }

    @GetMapping("/search")
    List<Item> search(@RequestParam String text){
        return itemService.searchAvailableItems(text);
    }



}
