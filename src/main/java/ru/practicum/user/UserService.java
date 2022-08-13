package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotExistedUserException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInMemoryRepository userRepository;
    private final UserJPARepository repository;

    public User create(User user) {
        userValidation(user);
        return repository.save(user);
    }

    private void userValidation(User user) {
        userRepository.getUsers().values().forEach(
                user1 -> {
                    if (user1.getEmail().equals(user.getEmail()))
                        throw new ValidationException();
                }
        );
    }

    public User update(User user)  {
        userValidation(user);
        return repository.save(userFieldsUpdate(user));
    }

    User userFieldsUpdate(User user){
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
            return repository.getById(userId);
    }

    public List<User> getAll() {
        return repository.findAll()
                .stream()
                .collect(Collectors.toList());
    }

    public void delete(long userId) {
        repository.deleteById(userId);
    }


}
