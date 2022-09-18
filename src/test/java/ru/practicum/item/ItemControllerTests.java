package ru.practicum.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.common.Mapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)

public class ItemControllerTests {

    @MockBean
    private ItemService itemService;

    @MockBean
    private Mapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    Item item = Item.builder()
            .id(1L)
            .name("name")
            .description("descript")
            .available(true)
            .build();

    ItemDTO itemDTO = ItemDTO.builder()
            .id(1L)
            .name("name")
            .description("descript")
            .available(true)
            .build();

    @Test
    public void shouldCreateItemTest() throws Exception {
        when(itemService.create(any(), anyLong()))
                .thenReturn(item);
        when(mapper.toItemDTO(item))
                .thenReturn(itemDTO);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDTO.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDTO.getDescription()), String.class));
    }

    @Test
    public void shouldUpdateItemTest() throws Exception {
        when(itemService.update(any(), anyLong(), anyLong()))
                .thenReturn(item);
        when(mapper.toItemDTO(item))
                .thenReturn(itemDTO);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(itemDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDTO.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDTO.getDescription()), String.class));
    }

    @Test
    public void shouldSearchItemsTest() throws Exception {
        when(itemService.searchAvailableItems(anyString()))
                .thenReturn(List.of(item));
        when(mapper.toItemDTO(item))
                .thenReturn(itemDTO);

        mvc.perform(get("/items/search")
                        .param("text", "requestTexts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemDTO.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDTO.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(itemDTO.getDescription()), String.class));
    }

    @Test
    public void shouldAddCommentTest() throws Exception {
        CommentDTO commentDTO = new CommentDTO(1L, "comment", "Alex", LocalDateTime.now());

        when(itemService.addComment(anyLong(), any(), anyLong()))
                .thenReturn(commentDTO);

        mvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDTO.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDTO.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentDTO.getAuthorName()), String.class));
    }

    @Test
    public void shouldGetItemByIdTest() throws Exception {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemDTO);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDTO.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDTO.getDescription()), String.class));
    }

    @Test
    public void shouldGetAllItemsOfOwner() throws Exception {
        when(itemService.getAllUserItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDTO));

        mvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "5")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemDTO.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDTO.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(itemDTO.getDescription()), String.class));
    }


}
