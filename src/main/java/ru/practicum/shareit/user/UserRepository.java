package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    UserDto addToStorage(UserDto user);

    UserDto updateUser(Long userId, UserDto user);

    Optional<User> getUserById(Long userId);

    Collection<UserDto> getAllUsers();

    void deleteUser(Long userId);
}
