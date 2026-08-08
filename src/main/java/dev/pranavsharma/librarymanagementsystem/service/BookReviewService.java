package dev.pranavsharma.librarymanagementsystem.service;

import dev.pranavsharma.librarymanagementsystem.payload.dto.BookReviewDTO;
import dev.pranavsharma.librarymanagementsystem.payload.request.CreateReviewRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.UpdateReviewRequest;
import dev.pranavsharma.librarymanagementsystem.payload.response.PageResponse;

public interface BookReviewService {

    BookReviewDTO createReview(CreateReviewRequest request) throws Exception;

    BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) throws Exception;

    void deleteReview(Long reviewId) throws Exception;

    PageResponse<BookReviewDTO> getReviewsByBookId(Long bookId, int page, int size) throws Exception;
}
