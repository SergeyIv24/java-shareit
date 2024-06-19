package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User addToStorage(User user);

    User updateUser(Long userId, User user);

    Optional<User> getUserById(Long userId);

    Collection<User> getAllUsers();

    void deleteUser(Long userId);


}
