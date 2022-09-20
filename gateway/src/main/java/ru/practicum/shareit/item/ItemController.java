package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.Mapper;
import ru.practicum.item.Comment;
import ru.practicum.item.CommentDTO;
import ru.practicum.item.ItemDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;
    private static final String header = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(header) long userId, @RequestBody @NonNull @Valid ItemDTO itemDTO) {
        return itemClient.create(itemDTO, userId);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(header) long userId, @RequestBody @NonNull ItemDTO itemDTO,
                          @PathVariable long itemId) {
        return itemClient.update(itemDTO, userId, itemId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader(header) long userId, @PathVariable("id") long itemId) {
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItem(@RequestHeader(header) long userId, @Min(0) @RequestParam (required = false)
    Integer from, @Min(1) @RequestParam (required = false) Integer size) {
        return itemClient.getAllUserItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text) {
        return itemClient.searchAvailableItems(text);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(header) long userId,
                                 @RequestBody @NonNull @Valid Comment comment, @PathVariable long itemId) {
        return itemClient.addComment(userId, comment, itemId);
    }


}
