package ru.practicum.user;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserInMemoryRepository {
    @Getter
    private Map<Long, User> users;

    private long counter;

    public UserInMemoryRepository() {
        this.users = new HashMap<>();
        this.counter = 1;
    }

    public void create(User user) {
        long id = getId();
        user.setId(id);
        users.put(id, user);
    }

    private long getId() {
        return counter++;
    }

    public User update(User user, long userId) throws IllegalAccessException {
        User userToUpdate = users.get(userId);
        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        return userToUpdate;
    }

    public User get(long userId) {
        return users.get(userId);
    }

    public Collection<User> getAll() {
        return users.values();
    }

    public User delete(long userId) {
        return users.remove(userId);
    }

}