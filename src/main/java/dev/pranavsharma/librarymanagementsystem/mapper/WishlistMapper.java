package dev.pranavsharma.librarymanagementsystem.mapper;

import dev.pranavsharma.librarymanagementsystem.model.Wishlist;
import dev.pranavsharma.librarymanagementsystem.payload.dto.WishlistDTO;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    private final BookMapper bookMapper;

    public WishlistMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public WishlistDTO toDTO(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }

        WishlistDTO dto = new WishlistDTO();
        dto.setId(wishlist.getId());

        if (wishlist.getUser() != null) {
            dto.setUserId(wishlist.getUser().getId());
            dto.setUserName(wishlist.getUser().getFullName());
        }

        if (wishlist.getBook() != null) {
            dto.setBook(bookMapper.toDTO(wishlist.getBook()));
        }

        dto.setAddedAt(wishlist.getAddedAt());
        dto.setNotes(wishlist.getNotes());

        return dto;
    }
}
