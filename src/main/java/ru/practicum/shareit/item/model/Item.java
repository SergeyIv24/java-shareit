package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long id;

    @NotBlank
    @Size(max = 100, message = "Bad name length")
    @Column(name = "name")
    String name;

    @NotBlank
    @Size(max = 200, message = "Bad description length")
    @Column(name = "description")
    String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @NotNull
    @Column(name = "available")
    Boolean available;
}
