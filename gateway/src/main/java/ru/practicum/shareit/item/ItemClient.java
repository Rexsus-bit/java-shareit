package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.item.Comment;
import ru.practicum.item.ItemDTO;
import ru.practicum.shareit.client.BaseClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(ItemDTO itemDTO, long userId) {
        return post("", userId, itemDTO);
    }

    public ResponseEntity<Object> update(ItemDTO itemDTO, long userId, long itemId) {
        return patch("/" + itemId, userId,itemDTO);
    }

    public ResponseEntity<Object> getItem(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllUserItems(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("from", from);
        parameters.put("size", size);
        return get("", userId, parameters);
    }

    public ResponseEntity<Object> searchAvailableItems(String text) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("text", text);
//        parameters.put("from", 0);
//        parameters.put("size", 2);
        return get("/search?text={text}", null, parameters);
    }

    public ResponseEntity<Object> addComment(long userId, Comment comment, long itemId) {
        return post("/" + itemId + "/comment",userId, comment);
    }
}
