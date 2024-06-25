package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User addToStorage(UserDto user);

    User updateUser(Long userId, UserDto user);

    Optional<User> getUserById(Long userId);

    Collection<User> getAllUsers();

    void deleteUser(Long userId);
}
