package dev.pranavsharma.librarymanagementsystem.repository;

import dev.pranavsharma.librarymanagementsystem.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("select s from Subscription s where s.user.id = :userId and " +
            "s.isActive = true and " +
            "s.startDate <= :today and s.endDate >= :today "
    )
    Optional<Subscription> findActiveSubscriptionByUserId(
            @Param("userId") Long userId,
            @Param("today")LocalDate today
            );

    @Query("select s from Subscription s where s.isActive = true and " +
            "s.endDate < :today "
    )
    List<Subscription> findExpiredActiveSubscriptions(
            @Param("today") LocalDate today
    );
}
