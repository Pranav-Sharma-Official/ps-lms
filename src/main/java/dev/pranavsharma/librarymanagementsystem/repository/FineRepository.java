package dev.pranavsharma.librarymanagementsystem.repository;

import dev.pranavsharma.librarymanagementsystem.domain.FineStatus;
import dev.pranavsharma.librarymanagementsystem.domain.FineType;
import dev.pranavsharma.librarymanagementsystem.model.Fine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {

    @Query("""
        select f from Fine f
        where (:userId is null or f.user.id = :userId)
        and (:status is null or f.status = :status)
        and (:type is null or f.type = :type)
        order by f.createdAt desc
    """)
    Page<Fine> findAllWithFilters(
            @Param("userId") Long userId,
            @Param("status")FineStatus status,
            @Param("type")FineType type,
            Pageable pageable
    );

    List<Fine> findByUserId(Long userId);

    List<Fine> findByUserIdAndType(Long userId, FineType type);
}
