package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {
    User createUser(User user);

    User updateUser(Long userId, User user);

    User getUserById(Long userId);

    Collection<User> getAllUsers();

    void deleteUser(Long userId);
}
