package com.example.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInMemoryRepository userRepository;

    public User create(User user) {
        userValidation(user);
        userRepository.create(user);
        return user;
    }

    private void userValidation(User user) {
        userRepository.getUsers().values().forEach(
                user1 -> {
                    if (user1.getEmail().equals(user.getEmail()))
                        throw new ValidationException();
                }
        );
    }

    public User update(User user, long userId) throws IllegalAccessException {
        userValidation(user);
        return userRepository.update(user, userId);
    }

    public User get(long userId) {
        return userRepository.get(userId);
    }

    public List<User> getAll() {
        return userRepository.getAll().stream()
                .collect(Collectors.toList());
    }

    public User delete(long userId) {
        return userRepository.delete(userId);
    }


}
