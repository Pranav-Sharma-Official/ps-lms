package dev.pranavsharma.librarymanagementsystem.service.impl;

import dev.pranavsharma.librarymanagementsystem.domain.BookLoanStatus;
import dev.pranavsharma.librarymanagementsystem.domain.ReservationStatus;
import dev.pranavsharma.librarymanagementsystem.domain.UserRole;
import dev.pranavsharma.librarymanagementsystem.mapper.ReservationMapper;
import dev.pranavsharma.librarymanagementsystem.model.Book;
import dev.pranavsharma.librarymanagementsystem.model.Reservation;
import dev.pranavsharma.librarymanagementsystem.model.User;
import dev.pranavsharma.librarymanagementsystem.payload.dto.ReservationDTO;
import dev.pranavsharma.librarymanagementsystem.payload.request.CheckoutRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.ReservationRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.ReservationSearchRequest;
import dev.pranavsharma.librarymanagementsystem.payload.response.PageResponse;
import dev.pranavsharma.librarymanagementsystem.repository.BookLoanRepository;
import dev.pranavsharma.librarymanagementsystem.repository.BookRepository;
import dev.pranavsharma.librarymanagementsystem.repository.ReservationRepository;
import dev.pranavsharma.librarymanagementsystem.service.BookLoanService;
import dev.pranavsharma.librarymanagementsystem.service.ReservationService;
import dev.pranavsharma.librarymanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final ReservationMapper reservationMapper;
    private final BookLoanService bookLoanService;

    int MAX_RESERVATIONS = 5;

    @Override
    public ReservationDTO createReservation(ReservationRequest reservationRequest) throws Exception {

        User user = userService.getCurrentUser();
        return createReservationForUser(reservationRequest, user.getId());
    }

    @Override
    public ReservationDTO createReservationForUser(ReservationRequest reservationRequest, Long userId) throws Exception {

        boolean alreadyHasLoan = bookLoanRepository.existsByUserIdAndBookIdAndStatus(
                userId, reservationRequest.getBookId(), BookLoanStatus.CHECKED_OUT
        );

        if (alreadyHasLoan) {
            throw new Exception("You already have a loan on this book!");
        }

        User user = userService.getCurrentUser();

        Book book = bookRepository.findById(reservationRequest.getBookId())
                .orElseThrow(() -> new Exception("Book not found!"));

        if (reservationRepository.hasActiveReservation(userId, book.getId())) {
            throw new Exception("You already have a reservation on this book!");
        }

        if (book.getAvailableCopies() > 0) {
            throw new Exception("Book is already available!");
        }

        long activeReservations = reservationRepository.countActiveReservationsByUser(userId);

        if (activeReservations >= MAX_RESERVATIONS) {
            throw new Exception("Reservation is full' and you've already reserved " +  MAX_RESERVATIONS + " times!");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setNotificationSent(false);
        reservation.setNotes(reservationRequest.getNotes());

        long pendingCount = reservationRepository.countPendingReservationsByBook(book.getId());
        reservation.setQueuePosition((int) pendingCount + 1);

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public ReservationDTO cancelReservation(Long reservationId) throws Exception {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with ID: " + reservationId));

        User user = userService.getCurrentUser();
        if (!reservation.getUser().getId().equals(user.getId()) && user.getRole() != UserRole.ROLE_ADMIN) {
            throw new Exception("You can only cancel your own reservations!");
        }

        if (!reservation.canBeCancelled()) {
            throw new Exception("Reservation can't be cancelled (current status is " + reservation.getStatus() + ")!");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public ReservationDTO fulfillReservation(Long reservationId) throws Exception {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with ID: " + reservationId));

        if (reservation.getBook().getAvailableCopies() <= 0) {
            throw new Exception("Reservation is not available for pickup (current status is "  + reservation.getStatus() + ")!");
        }

        reservation.setStatus(ReservationStatus.FULFILLED);
        reservation.setCancelledAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        CheckoutRequest request = new CheckoutRequest();
        request.setBookId(reservation.getBook().getId());
        request.setNotes("Assigned by Admin!");

        bookLoanService.checkoutBookForUser(reservation.getUser().getId(), request);

        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest searchRequest) throws Exception {

        User user = userService.getCurrentUser();
        searchRequest.setUserId(user.getId());
        return searchReservations(searchRequest);
    }

    @Override
    public PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest searchRequest) {

        Pageable pageable = createPageable(searchRequest);

        Page<Reservation> reservationPage = reservationRepository.searchReservationsWithFilters(
                searchRequest.getUserId(),
                searchRequest.getBookId(),
                searchRequest.getStatus(),
                searchRequest.getActiveOnly() != null ? searchRequest.getActiveOnly() : false,
                pageable
        );

        return buildPageResponse(reservationPage);
    }

    public PageResponse<ReservationDTO> buildPageResponse(Page<Reservation> reservationPage) {
        List<ReservationDTO> DTOs = reservationPage.getContent().stream()
                .map(reservationMapper::toDTO)
                .toList();

        PageResponse<ReservationDTO> response = new PageResponse<>();
        response.setContent(DTOs);
        response.setPageNumber(reservationPage.getNumber());
        response.setPageSize(reservationPage.getSize());
        response.setTotalElements(reservationPage.getTotalElements());
        response.setTotalPages(reservationPage.getTotalPages());
        response.setLast(reservationPage.isLast());

        return response;
    }

    private Pageable createPageable(ReservationSearchRequest searchRequest) {
        Sort sort = "ASC".equalsIgnoreCase(searchRequest.getSortDirection())
                ? Sort.by(searchRequest.getSortBy()).ascending()
                : Sort.by(searchRequest.getSortBy()).descending();

        return PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);
    }
}
