package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto user);

    UserDto updateUser(Long userId, UserDto user);

    UserDto getUserById(Long userId);

    Collection<UserDto> getAllUsers();

    void deleteUser(Long userId);
}
