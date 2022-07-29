package com.example.shareit.item;

import com.example.shareit.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    ItemDTO create(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @NonNull @Valid ItemDTO itemDTO) {
        return Mapper.toItemDto(itemService.create(Mapper.toItem(itemDTO), userId));
    }

    @PatchMapping("{itemId}")
    ItemDTO update(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @NonNull ItemDTO itemDTO,
                   @PathVariable long itemId) {
        return Mapper.toItemDto(itemService.update(Mapper.toItem(itemDTO), userId, itemId));
    }

    @GetMapping("/{id}")
    ItemDTO getItem(@PathVariable("id") long itemId) {
        return Mapper.toItemDto(itemService.getItem(itemId));
    }

    @GetMapping
    List<ItemDTO> getAllUserItem(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllUserItems(userId).stream().map(Mapper::toItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    List<ItemDTO> search(@RequestParam String text) {
        return itemService.searchAvailableItems(text).stream().map(Mapper::toItemDto)
                .collect(Collectors.toList());
    }


}
