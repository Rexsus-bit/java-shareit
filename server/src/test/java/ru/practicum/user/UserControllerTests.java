package ru.practicum.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mvc;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        userDTO = new UserDTO(
                1L,
                "Andrey",
                "andrey.snith@gmail.com"
        );
        user = new User(
                1L,
                "Den",
                "r.ssa@gmail.com"
        );
    }

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
                .andExpect(jsonPath("$.name", is(userDTO.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDTO.getEmail()), String.class));
    }



}
