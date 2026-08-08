package dev.pranavsharma.librarymanagementsystem.repository;

import dev.pranavsharma.librarymanagementsystem.model.Book;
import dev.pranavsharma.librarymanagementsystem.payload.request.BookSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("select b from Book b where " +
            "(:searchTerm is null or " +
            "lower(b.title) like lower(concat('%', :searchTerm, '%')) or " +
            "lower(b.author) like lower(concat('%', :searchTerm, '%')) or " +
            "lower(b.isbn) like lower(concat('%', :searchTerm, '%'))) and " +
            "(:genreId is null or b.genre.id=:genreId) and " +
            "(:availableOnly = false or b.availableCopies > 0) and " +
            "b.active = true"
    )
    Page<Book> searchBooksWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("genreId") Long genreId,
            @Param("availableOnly") boolean availableOnly,
            Pageable pageable
    );

    long countByActiveTrue();

    @Query("select count(b) from Book b where b.availableCopies > 0 and b.active = true")
    long countAvailableBooks();

}
