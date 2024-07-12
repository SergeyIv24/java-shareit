package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userServiceImpl;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto createNewUser(@Valid @RequestBody UserDto user) {
        return userServiceImpl.createUser(user);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@Valid @PathVariable(value = "userId") long id, @RequestBody UserDto user) {
        return userServiceImpl.updateUser(id, user);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable long userId) {
        return userServiceImpl.getUserById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long userId) {
        userServiceImpl.deleteUser(userId);
    }
}
