package dev.pranavsharma.librarymanagementsystem.repository;

import dev.pranavsharma.librarymanagementsystem.domain.ReservationStatus;
import dev.pranavsharma.librarymanagementsystem.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(" select case when count(r) > 0 then true else false end from Reservation r " +
            " where r.user.id = :userId or r.book.id = :bookId " +
            " and (r.status = 'PENDING' or r.status = 'AVAILABLE') ")
    boolean hasActiveReservation(@Param("userId") Long userId, @Param("bookId") Long bookId);

    @Query(" select count(r) from Reservation r where r.user.id = :userId " +
            " and (r.status = 'PENDING' or r.status = 'AVAILABLE') ")
    long countActiveReservationsByUser(@Param("userId") Long userId);

    @Query(" select count(r) from Reservation r where r.book.id = :bookId " +
            " and r.status = 'PENDING' ")
    long countPendingReservationsByBook(@Param("bookId") Long bookId);

    @Query(" select r from Reservation r where " +
            " (:userId is null or r.user.id = :userId) and " +
            " (:bookId is null or r.book.id = :bookId) and " +
            " (:status is null or r.status = :status) and " +
            " (:activeOnly = false or (r.status = 'PENDING' or r.status = 'AVAILABLE')) ")
    Page<Reservation> searchReservationsWithFilters(
            @Param("userId") Long userId,
            @Param("bookId") Long bookId,
            @Param("status") ReservationStatus status,
            @Param("activeOnly") boolean activeOnly,
            Pageable pageable
    );
}
