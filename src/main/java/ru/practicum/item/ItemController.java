package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Mapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final Mapper mapper;
    private final String HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDTO create(@RequestHeader(HEADER) long userId, @RequestBody @NonNull @Valid ItemDTO itemDTO) {

        return mapper.toItemDto(itemService.create(Mapper.toItem(itemDTO), userId));
    }

    @PatchMapping("{itemId}")
    public ItemDTO update(@RequestHeader(HEADER) long userId, @RequestBody @NonNull ItemDTO itemDTO,
                          @PathVariable long itemId) {
        return mapper.toItemDto(itemService.update(Mapper.toItem(itemDTO), userId, itemId));
    }

    @GetMapping("/{id}")
    public ItemDTO getItem(@RequestHeader(HEADER) long userId, @PathVariable("id") long itemId) {
        return itemService.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemDTO> getAllUserItem(@RequestHeader(HEADER) long userId) {
        return itemService.getAllUserItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDTO> search(@RequestParam String text) {
        return itemService.searchAvailableItems(text)
                .stream()
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping("{itemId}/comment")
    public CommentDTO addComment(@RequestHeader(HEADER) long userId,
                                 @RequestBody @NonNull @Valid Comment comment, @PathVariable long itemId) {
        return itemService.addComment(userId, comment, itemId);
    }


}
