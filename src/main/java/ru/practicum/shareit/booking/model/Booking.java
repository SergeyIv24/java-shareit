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
    Long id;

    @NotNull(message = "itemId must exist")
    @OneToOne
    @JoinColumn(name = "item_id")
    Item item;

    @NotNull(message = "userId must exist")
    @ManyToOne
    @JoinColumn(name = "user_id")
    User booker; //Who takes item

    @Column(name = "start_date")
    Instant start;


    @Column(name = "end_date")
    Instant end;

    @Column(name = "status")
    String status;
}
