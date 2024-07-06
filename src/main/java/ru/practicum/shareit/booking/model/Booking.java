package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

/*@Entity
@Table(name = "booking")*/
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long bookingId;

    @NotNull(message = "itemId must exist")

    Long itemId;

    @NotNull(message = "userId must exist")
    Long userId; //Who takes item
    LocalDate start;
    Duration rentDuration;
    LocalDate end;

    @Column(name = "is_confirmed")
    Boolean isConfirmed;
}
