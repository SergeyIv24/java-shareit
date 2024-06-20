package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userServiceImpl;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User createNewUser(@Valid @RequestBody User user) {
        return userServiceImpl.createUser(user);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @PathVariable(value = "userId") Long id, @RequestBody User user) {
        return userServiceImpl.updateUser(id, user);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Long userId) {
        return userServiceImpl.getUserById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long userId) {
        userServiceImpl.deleteUser(userId);
    }
}
