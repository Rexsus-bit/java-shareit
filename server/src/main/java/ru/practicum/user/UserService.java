package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.common.Mapper;
import ru.practicum.exceptions.NotExistedUserException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository repository;

    public UserDTO create(User user) {
        return Mapper.toUserDto(repository.save(user));
    }

    public UserDTO update(User user) {
        return Mapper.toUserDto(repository.save(userFieldsUpdate(user)));
    }

    User userFieldsUpdate(User user) {
        User userToUpdate = repository.getById(user.getId());
        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        return userToUpdate;
    }

    public User get(long userId) {
        return repository.findById(userId).orElseThrow(NotExistedUserException::new);
    }

    public List<User> getAll() {
        return new ArrayList<>(repository.findAll());
    }

    public void delete(long userId) {
        repository.deleteById(userId);
    }


}
