package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, String status);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, Instant current);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, Instant current);

    List<Booking> findByBookerIdAndItemIdAndStatus(Long bookerId, Long itemId, String status);

    @Query(value = "SELECT * FROM bookings " +
            "WHERE start_date <= ?2 " +
            "AND end_date >= ?2 " +
            "AND user_id = ?1 " +
            "ORDER BY start_date ASC ", nativeQuery = true)
    List<Booking> findCurrentBookings(Long bookerId, Instant now);


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
            "AND status NOT IN ('REJECTED') " +
            "ORDER BY start_date DESC ", nativeQuery = true)
    List<Booking> findByOwnerIdStatusPastAndOrderByStartDesc(Long ownerId, Instant now);

    @Query(value = "SELECT b.id, b.item_id, b.user_id, start_date, " +
            "end_date, status " +
            "FROM bookings AS b " +
            "JOIN items AS i ON b.item_id = i.item_id " +
            "GROUP BY b.id, i.user_id " +
            "Having i.user_id = ?1 AND start_date <= ?2 " +
            "AND end_date >= ?2 " +
            "ORDER BY start_date DESC ", nativeQuery = true)
    List<Booking> findByOwnerIdStatusCurrentAndOrderByStartDesc(Long ownerId, Instant now);

    @Query(value = "select  * from bookings " +
            "where item_id = ?1 AND start_date < ?2 " +
            "AND status IN ('APPROVED') " +
            "ORDER BY start_date DESC " +
            "limit 1 ", nativeQuery = true)
    Optional<Booking> findLastBooking(Long itemId, Instant now);

    @Query(value = "select  * from bookings " +
            "where item_id = ?1 AND start_date > ?2 " +
            "AND status IN ('APPROVED') " +
            "ORDER BY start_date ASC " +
            "limit 1 ", nativeQuery = true)
    Optional<Booking> findNextBooking(Long itemId, Instant now);
}
