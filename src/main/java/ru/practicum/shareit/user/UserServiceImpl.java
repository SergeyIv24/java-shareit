package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepositoryInMemory;

    @Override
    public User createUser(User user) {
        validateUserEmailDuplicate(user);
        return userRepositoryInMemory.addToStorage(user);
    }

    @Override
    public User updateUser(Long userId, User user) {
        user.setId(userId);
        validateUserEmailDuplicate(user);
        return userRepositoryInMemory.updateUser(userId, user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepositoryInMemory.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("user is not found"));
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepositoryInMemory.getAllUsers();
    }

    @Override
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
            throw new ConflictException("DuplicateEmail");
        }
    }
}
