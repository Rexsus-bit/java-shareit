package ru.practicum.user;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTests {

    private final UserService userService;

    @Test
    public void shouldSaveUser() {

        User user = new User(null, "James", "james@yandex.com");
        UserDTO userDTO = userService.create(user);

        assertThat(userDTO, hasProperty("id", equalTo(1L)));
        assertThat(new User(1L, "James", "james@yandex.com"), equalTo(userService.get(1L)));
    }

}