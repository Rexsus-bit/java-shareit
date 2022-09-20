package ru.practicum.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.common.Mapper;
import ru.practicum.item.Item;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTests {


    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private Mapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    Item item = Item.builder()
            .id(1L)
            .name("name")
            .description("отверТка")
            .available(true)
            .build();

    ItemRequest itemRequest = ItemRequest.builder()
        .id(1L)
        .description("dd")
        .build();
    ItemRequestDTO itemRequestDTO = ItemRequestDTO.builder()
            .id(1L)
            .description("dd")
            .created(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
            .items(List.of(item))
            .build();

    @Test
    public void shouldCreateItemRequestTest() throws Exception {
        when(itemRequestService.createItemRequest(anyLong(), ArgumentMatchers.any()))
                .thenReturn(itemRequestDTO);

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDTO.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDTO.getDescription()), String.class))
                .andExpect(jsonPath("$.requesterId", is(itemRequestDTO.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.created", is(itemRequestDTO.getCreated().toString()), String.class));
    }

    @Test
    public void shouldGetItemRequestById() throws Exception {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDTO);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDTO.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDTO.getDescription()), String.class))
                .andExpect(jsonPath("$.created", is(itemRequestDTO.getCreated().toString()), String.class))
                .andExpect(jsonPath("$.items[0]", is(item), Item.class))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    public void shouldGetAllByUserId() throws Exception {
        when(itemRequestService.getItemRequestsOfUser(anyLong()))
                .thenReturn(List.of(itemRequestDTO));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemRequestDTO.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDTO.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].created", is(itemRequestDTO.getCreated().toString()), String.class))
                .andExpect(jsonPath("$.[0].items[0]", is(item), Item.class))
                .andExpect(jsonPath("$.[0].items", hasSize(1)));
    }

    @Test
    public void shouldGetAllOfOthersInPages() throws Exception {
        when(itemRequestService.getItemRequestsOfOther(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDTO));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 10L)
                        .param("from", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemRequestDTO.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(itemRequestDTO.getDescription()), String.class))
                .andExpect(jsonPath("$.[0].created", is(itemRequestDTO.getCreated().toString()), String.class))
                .andExpect(jsonPath("$.[0].items[0]", is(item), Item.class))
                .andExpect(jsonPath("$.[0].items", hasSize(1)));
    }

}
