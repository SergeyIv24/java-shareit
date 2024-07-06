package ru.practicum.shareit.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Empty user name")
    @Size(min = 1, max = 20, message = "Bad user name length")
    @Column(name = "name")
    String name;

    @NotBlank(message = "Empty email name")
    @Email(message = "Is not correct email pattern")
    @Column(name = "email")
    String email;

    //Collection<Item> items;
}
