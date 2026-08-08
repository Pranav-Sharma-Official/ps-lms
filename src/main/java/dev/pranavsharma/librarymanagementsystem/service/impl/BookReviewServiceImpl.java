package dev.pranavsharma.librarymanagementsystem.service.impl;

import dev.pranavsharma.librarymanagementsystem.domain.BookLoanStatus;
import dev.pranavsharma.librarymanagementsystem.mapper.BookReviewMapper;
import dev.pranavsharma.librarymanagementsystem.model.Book;
import dev.pranavsharma.librarymanagementsystem.model.BookLoan;
import dev.pranavsharma.librarymanagementsystem.model.BookReview;
import dev.pranavsharma.librarymanagementsystem.model.User;
import dev.pranavsharma.librarymanagementsystem.payload.dto.BookReviewDTO;
import dev.pranavsharma.librarymanagementsystem.payload.request.CreateReviewRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.UpdateReviewRequest;
import dev.pranavsharma.librarymanagementsystem.payload.response.PageResponse;
import dev.pranavsharma.librarymanagementsystem.repository.BookLoanRepository;
import dev.pranavsharma.librarymanagementsystem.repository.BookRepository;
import dev.pranavsharma.librarymanagementsystem.repository.BookReviewRepository;
import dev.pranavsharma.librarymanagementsystem.service.BookReviewService;
import dev.pranavsharma.librarymanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewMapper bookReviewMapper;
    private final BookLoanRepository bookLoanRepository;

    @Override
    public BookReviewDTO createReview(CreateReviewRequest request) throws Exception {

        User user = userService.getCurrentUser();

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new Exception("Book not found!"));

        if (bookReviewRepository.existsByUserIdAndBookId(user.getId(), book.getId())) {
            throw new Exception("You have already reviewed this book!");
        }

        boolean hasReadBook = hasUserReadBook(user.getId(), book.getId());

        if (!hasReadBook) {
            throw new Exception("You have not read this book!");
        }

        BookReview bookReview = new BookReview();
        bookReview.setUser(user);
        bookReview.setBook(book);
        bookReview.setRating(request.getRating());
        bookReview.setReviewText(request.getReviewText());
        bookReview.setTitle(request.getTitle());

        BookReview savedBookReview = bookReviewRepository.save(bookReview);
        return bookReviewMapper.toDTO(savedBookReview);
    }

    @Override
    public BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) throws Exception {

        User user = userService.getCurrentUser();

        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception("Review not found!"));

        if (!bookReview.getUser().getId().equals(user.getId())) {
            throw new Exception("You have not reviewed this book!");
        }

        bookReview.setReviewText(request.getReviewText());
        bookReview.setTitle(request.getTitle());
        bookReview.setRating(request.getRating());
        BookReview savedBookReview = bookReviewRepository.save(bookReview);

        return bookReviewMapper.toDTO(savedBookReview);
    }

    @Override
    public void deleteReview(Long reviewId) throws Exception {

        User user = userService.getCurrentUser();

        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new Exception("Review not found with ID: " + reviewId));

        if (!bookReview.getUser().getId().equals(user.getId())) {
            throw new Exception("You can only delete your own review!");
        }

        bookReviewRepository.delete(bookReview);
    }

    @Override
    public PageResponse<BookReviewDTO> getReviewsByBookId(Long id, int page, int size) throws Exception {

        Book book = bookRepository.findById(id).orElseThrow(() -> new Exception("Book not found with ID: " + id));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<BookReview> reviewPage = bookReviewRepository.findByBook(book, pageable);
        return convertToPageResponse(reviewPage);
    }

    private boolean hasUserReadBook(Long userId, Long bookId) {

        List<BookLoan> bookLoans = bookLoanRepository.findByBookId(bookId);
        return bookLoans.stream().anyMatch(loan -> loan.getUser().getId().equals(userId) && loan.getStatus() == BookLoanStatus.RETURNED);
    }

    private PageResponse<BookReviewDTO> convertToPageResponse(Page<BookReview> reviewPage) {

        List<BookReviewDTO> reviewDTOs = reviewPage.getContent()
                .stream()
                .map(bookReviewMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                reviewDTOs,
                reviewPage.getNumber(),
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewPage.isLast(),
                reviewPage.isFirst(),
                reviewPage.isEmpty()
        );
    }
}
