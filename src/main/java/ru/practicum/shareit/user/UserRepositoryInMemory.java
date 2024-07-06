/*
package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.*;


@Repository
@Slf4j
public class UserRepositoryInMemory implements UserRepository {
    private static Long userAmount = 0L;
    private final List<User> users = new ArrayList<>();

    @Override
    public User addToStorage(UserDto newUser) {
        User user = UserMapper.mapToUser(newUser);
        if (newUser.getId() == null) {
            user.setId(defineUserId());
        }
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(Long userId, UserDto user) {
        User updatedUser = getUserById(userId).orElseThrow();
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        deleteUser(userId);
        addToStorage(UserMapper.mapToUserDto(updatedUser));
        return updatedUser;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public void deleteUser(Long userId) {
        users.removeIf(user -> user.getId().equals(userId));
    }

    private Long defineUserId() {
        if (users.isEmpty()) {
            userAmount = 1L;
            return userAmount;
        }
        return ++userAmount;
    }
}
*/
