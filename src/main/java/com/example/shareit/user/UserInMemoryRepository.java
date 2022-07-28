package com.example.shareit.user;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

    public User update(User user, long userId) {

        User userToUpdate =  users.get(userId);

        try {
            for (Field field : User.class.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object val = field.get(user);
                    if (val != null) {
                        field.set(userToUpdate, val);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            // Handle exception
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