package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto user) {
        validateUserEmailDuplicate(user);
        return UserMapper.mapToUserDto(userRepository.save(UserMapper.mapToUser(user)));

        //return UserMapper.mapToUserDto(userRepositoryInMemory.addToStorage(user));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto user) {
        user.setId(userId);
        validateUserEmailDuplicate(user);

        User updatedUser = userRepository.findById(userId).orElseThrow();

        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }

        //userRepository.deleteById(userId);

        return UserMapper.mapToUserDto(userRepository.save(updatedUser));
        //return UserMapper.mapToUserDto(userRepository.updateUser(userId, user));
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.mapToUserDto(userRepository
                        .findById(userId)
                        .orElseThrow(() -> new NotFoundException("user is not found")));

/*        return userRepository.getUserById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("user is not found"));*/
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());

/*        return userRepository
                .getAllUsers()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());*/
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        //userRepository.deleteUser(userId);
    }

    private void validateUserEmailDuplicate(UserDto user) {
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
