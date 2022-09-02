package ru.practicum.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.common.Mapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTestWithContext {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Mapper mapper;

    UserDTO userDTO = new UserDTO(1L, "username", "email@ya.ru");

    User user = new User(1L, "Tom", "test@es.we");


    @Test
    void shouldCreateUserTest() throws Exception {
        when(userService.create(user))
                .thenReturn(userDTO);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDTO.getId()), long.class))
                .andExpect(jsonPath("$.name", is(userDTO.getName())))
                .andExpect(jsonPath("$.email", is(userDTO.getEmail())));
    }

    @Test
    void shouldCreateEmptyUserFailTest() throws Exception {
        user = new User(1L, null, null);
        when(userService.create(user))
                .thenReturn(userDTO);

        mvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldGetUserByIdTest() throws Exception {
        user = new User(1L, "Tom", "test@es.we");
        userDTO = new UserDTO(1L, "Tom", "test@es.we");
        when(userService.get(1))
                .thenReturn(user);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDTO.getId()), long.class))
                .andExpect(jsonPath("$.name", is(userDTO.getName())))
                .andExpect(jsonPath("$.email", is(userDTO.getEmail())));
    }
    @Test
    void shouldGetAllUsersTest() throws Exception {
        user = new User(1L, "Tom", "test@es.we");
        userDTO = new UserDTO(1L, "Tom", "test@es.we");
        when(userService.getAll())
                .thenReturn(List.of(user, new User(2L, "John", "test@es.we1")));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(1L), long.class))
                .andExpect(jsonPath("$.[1].id", is(2L), long.class))
                .andExpect(jsonPath("$.[0].name", is("Tom"), String.class))
                .andExpect(jsonPath("$.[1].name", is("John"), String.class))
                .andExpect(jsonPath("$.[0].email", is("test@es.we"), String.class))
                .andExpect(jsonPath("$.[1].email", is("test@es.we1"), String.class));
    }




}
