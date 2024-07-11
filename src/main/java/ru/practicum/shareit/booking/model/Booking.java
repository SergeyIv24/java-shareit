package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "itemId must exist")
    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @NotNull(message = "userId must exist")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User booker; //Who takes item

    @Column(name = "start_date")
    private Instant start;

    @Column(name = "end_date")
    private Instant end;

    @Column(name = "status")
    private String status;
}
