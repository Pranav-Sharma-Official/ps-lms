package dev.pranavsharma.librarymanagementsystem.service;

import dev.pranavsharma.librarymanagementsystem.domain.BookLoanStatus;
import dev.pranavsharma.librarymanagementsystem.payload.dto.BookLoanDTO;
import dev.pranavsharma.librarymanagementsystem.payload.request.BookLoanSearchRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.CheckinRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.CheckoutRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.RenewalRequest;
import dev.pranavsharma.librarymanagementsystem.payload.response.PageResponse;

public interface BookLoanService {

    BookLoanDTO checkoutBook(CheckoutRequest checkoutRequest) throws Exception;

    BookLoanDTO checkoutBookForUser(Long userId, CheckoutRequest checkoutRequest) throws Exception;

    BookLoanDTO checkinBook(CheckinRequest checkinRequest) throws Exception;

    BookLoanDTO renewCheckout(RenewalRequest renewalRequest) throws Exception;

    PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int pageSize) throws Exception;

    PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request) throws Exception;

    int updateOverdueBookLoan();

}
