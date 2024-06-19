package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepositoryInMemory userRepositoryInMemory;

    public User createUser(User user) {
        validateUserEmailDuplicate(user);
        return userRepositoryInMemory.addToStorage(user);
    }

    public User updateUser(Long userId, User user) {
        user.setId(userId);
        validateUserEmailDuplicate(user);
        return userRepositoryInMemory.updateUser(userId, user);
    }

    public User getUserById(Long userId) {
        return userRepositoryInMemory.getUserById(userId).orElseThrow();
    }

    public Collection<User> getAllUsers() {
        return userRepositoryInMemory.getAllUsers();
    }

    public void deleteUser(Long userId) {
        userRepositoryInMemory.deleteUser(userId);
    }

    private void validateUserEmailDuplicate(User user) {
        boolean isDuplicateEmail = getAllUsers()
                .stream()
                .filter(user1 -> !user1.getId().equals(user.getId()) && user1.getEmail().equals(user.getEmail()))
                .findFirst()
                .isEmpty();
        if (!isDuplicateEmail) {
            log.warn("Email is existed");
            throw new RuntimeException();
        }
    }
}
