package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto user);

    UserDto updateUser(long userId, UserDto user);

    UserDto getUserById(long userId);

    Collection<UserDto> getAllUsers();

    void deleteUser(long userId);
}
