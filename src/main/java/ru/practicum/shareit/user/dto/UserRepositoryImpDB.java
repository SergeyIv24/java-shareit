package ru.practicum.shareit.user.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

public interface UserRepositoryImpDB extends JpaRepository <User, Long> {
}
