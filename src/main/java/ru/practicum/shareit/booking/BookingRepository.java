package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, String status);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, Instant current);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, Instant current);

    @Query(value = "SELECT * FROM bookings " +
            "WHERE start_date < CURRENT_DATE " +
            "AND end_date < CURRENT_DATE " +
            "AND status IN ('APPROVED') " +
            "ORDER BY start_date DESC ", nativeQuery = true)
    List<Booking> findCurrentBookings(Long bookerId);


    @Query(value = "SELECT b.id, b.item_id, b.user_id, start_date, " +
            "end_date, status " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "GROUP BY b.id, i.user_id " +
            "Having i.user_id = ?1 AND status IN ('APPROVED', 'WAITING') " +
            "ORDER BY start_date DESC ", nativeQuery = true)
    List<Booking> findByOwnerIdOrderByStartDesc(Long userId);

    @Query(value = "SELECT b.id, b.item_id, b.user_id, start_date, " +
            "end_date, status " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "GROUP BY b.id, i.user_id " +
            "Having i.user_id = ?1 AND status IN ('REJECTED') " +
            "ORDER BY start_date DESC ",
            nativeQuery = true)
    List<Booking> findByOwnerIdStatusRejectedAndOrderByStartDesc(Long ownerId);

    @Query(value = "SELECT b.id, b.item_id, b.user_id, start_date, " +
            "end_date, status " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "GROUP BY b.id, i.user_id " +
            "Having i.user_id = ?1 AND status IN ('WAITING') " +
            "ORDER BY start_date DESC ",
            nativeQuery = true)
    List<Booking> findByOwnerIdStatusWaitingAndOrderByStartDesc(Long ownerId);

    @Query(value = "SELECT b.id, b.item_id, b.user_id, start_date, " +
            "end_date, status " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "GROUP BY b.id, i.user_id " +
            "Having i.user_id = ?1 AND start_date > ?2 " +
            "ORDER BY start_date DESC ", nativeQuery = true)
    List<Booking> findByOwnerIdStatusFutureAndOrderByStartDesc(Long ownerId, Instant now);

    @Query(value = "SELECT b.id, b.item_id, b.user_id, start_date, " +
            "end_date, status " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "GROUP BY b.id, i.user_id " +
            "Having i.user_id = ?1 AND start_date < ?2 " +
            "ORDER BY start_date DESC ", nativeQuery = true)
    List<Booking> findByOwnerIdStatusPastAndOrderByStartDesc(Long ownerId, Instant now);

    @Query(value = "SELECT b.id, b.item_id, b.user_id, start_date, " +
            "end_date, status " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "GROUP BY b.id, i.user_id " +
            "Having i.user_id = ?1 AND start_date < ?2 " +
            "AND end_date < ?2 " +
            "ORDER BY start_date DESC ", nativeQuery = true)
    List<Booking> findByOwnerIdStatusCurrentAndOrderByStartDesc(Long ownerId, Instant now);


}
