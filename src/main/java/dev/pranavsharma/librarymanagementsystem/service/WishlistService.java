package dev.pranavsharma.librarymanagementsystem.service;

import dev.pranavsharma.librarymanagementsystem.payload.dto.WishlistDTO;
import dev.pranavsharma.librarymanagementsystem.payload.response.PageResponse;

public interface WishlistService {

    WishlistDTO addToWishlist(Long bookId, String notes) throws Exception;

    void removeFromWishlist(Long bookId) throws Exception;

    PageResponse<WishlistDTO> getMyWishlist(int  page, int size) throws Exception;
}
