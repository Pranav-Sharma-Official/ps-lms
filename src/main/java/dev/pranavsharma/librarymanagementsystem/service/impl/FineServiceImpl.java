package dev.pranavsharma.librarymanagementsystem.service.impl;

import dev.pranavsharma.librarymanagementsystem.domain.FineStatus;
import dev.pranavsharma.librarymanagementsystem.domain.FineType;
import dev.pranavsharma.librarymanagementsystem.domain.PaymentGateway;
import dev.pranavsharma.librarymanagementsystem.domain.PaymentType;
import dev.pranavsharma.librarymanagementsystem.mapper.FineMapper;
import dev.pranavsharma.librarymanagementsystem.model.BookLoan;
import dev.pranavsharma.librarymanagementsystem.model.Fine;
import dev.pranavsharma.librarymanagementsystem.model.User;
import dev.pranavsharma.librarymanagementsystem.payload.dto.FineDTO;
import dev.pranavsharma.librarymanagementsystem.payload.request.CreateFineRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.PaymentInitiateRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.WaivedFineRequest;
import dev.pranavsharma.librarymanagementsystem.payload.response.PageResponse;
import dev.pranavsharma.librarymanagementsystem.payload.response.PaymentInitiateResponse;
import dev.pranavsharma.librarymanagementsystem.repository.BookLoanRepository;
import dev.pranavsharma.librarymanagementsystem.repository.FineRepository;
import dev.pranavsharma.librarymanagementsystem.service.FineService;
import dev.pranavsharma.librarymanagementsystem.service.PaymentService;
import dev.pranavsharma.librarymanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final BookLoanRepository bookLoanRepository;
    private final FineRepository fineRepository;
    private final FineMapper fineMapper;
    private final UserService userService;
    private final PaymentService paymentService;

    @Override
    public FineDTO createFine(CreateFineRequest createFineRequest) throws Exception {

//        1. Validate whether the book loan exists or not
        BookLoan bookLoan = bookLoanRepository.findById(createFineRequest.getBookLoanId())
                .orElseThrow(
                        () -> new Exception("Book loan doesn't exist!")
                );

//        2. Create fine

        Fine fine = Fine
                .builder()
                .bookLoan(bookLoan)
                .user(bookLoan.getUser())
                .type(createFineRequest.getType())
                .amount(createFineRequest.getAmount())
                .status(FineStatus.PENDING)
                .reason(createFineRequest.getReason())
                .notes(createFineRequest.getNotes())
                .build();

        Fine savedFine = fineRepository.save(fine);
        return fineMapper.toDTO(savedFine);
    }

    @Override
    public PaymentInitiateResponse payFine(Long fineId, String transactionId) throws Exception {

//        1. Validate whether the fine exists or not
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new Exception("Fine doesn't exist with ID: " + fineId));

//        2. Check if fine already paid or not
        if (fine.getStatus() == FineStatus.PAID) {
            throw new Exception("Fine is already paid!");
        }

//        3. Check if fine is waived off or not
        if (fine.getStatus() == FineStatus.WAIVED) {
            throw new Exception("Fine is already waived off!");
        }

//        4. Initiate payment
        User user = userService.getCurrentUser();
        PaymentInitiateRequest request = PaymentInitiateRequest.builder()
                .userId(user.getId())
                .fineId(fine.getId())
                .paymentType(PaymentType.FINE)
                .gateway(PaymentGateway.RAZORPAY)
                .amount(fine.getAmount())
                .description("Library fine payment.")
                .build();

        return paymentService.initiatePayment(request);
    }

    @Override
    public void markFineAsPaid(Long fineId, Long amount, String transactionId) throws Exception {

        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new Exception("Fine not found with ID: " + fineId));

//        Apply payment amount safely
        fine.applyPayment(amount);
        fine.setTransactionId(transactionId);
        fine.setStatus(FineStatus.PAID);
        fine.setUpdatedAt(LocalDateTime.now());

        fineRepository.save(fine);
    }

    @Override
    public FineDTO waivedFine(WaivedFineRequest waivedFineRequest) throws Exception {
        Fine fine = fineRepository.findById(waivedFineRequest.getFineId())
                .orElseThrow(() -> new Exception("Fine not found with ID: " + waivedFineRequest.getFineId()));

        if (fine.getStatus() == FineStatus.WAIVED) {
            throw new Exception("Fine is already waived off!");
        }

        if (fine.getStatus() == FineStatus.PAID) {
            throw new Exception("Fine is already paid and cannot be waived off!");
        }

        User currentAdmin = userService.getCurrentUser();
        fine.waive(currentAdmin, waivedFineRequest.getReason());

        Fine savedFine = fineRepository.save(fine);
        return fineMapper.toDTO(savedFine);
    }

    @Override
    public List<FineDTO> getMyFines(FineStatus status, FineType type) throws Exception {
        User user = userService.getCurrentUser();
        List<Fine> fines;

        if (status != null && type != null) {
            fines = fineRepository.findByUserId(user.getId()).stream()
                    .filter(f -> f.getStatus() == status && f.getType() == type)
                    .collect(Collectors.toList());
        } else if (status != null) {
            fines = fineRepository.findByUserId(user.getId()).stream()
                    .filter(f -> f.getStatus() == status)
                    .collect(Collectors.toList());
        } else if (type != null) {
            fines = fineRepository.findByUserIdAndType(user.getId(), type);
        } else {
            fines = fineRepository.findByUserId(user.getId());
        }
        return fines.stream().map(
                fineMapper::toDTO
        ).collect(Collectors.toList());
    }

    @Override
    public PageResponse<FineDTO> getAllFines(FineStatus status, FineType type, Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending());

        Page<Fine> finePage = fineRepository.findAllWithFilters(
                userId,
                status,
                type,
                pageable
        );
        return convertToPageResponse(finePage);
    }

    private PageResponse<FineDTO> convertToPageResponse(Page<Fine> finePage) {
        List<FineDTO> fineDTOS = finePage.getContent()
                .stream()
                .map(fineMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                fineDTOS,
                finePage.getNumber(),
                finePage.getSize(),
                finePage.getTotalElements(),
                finePage.getTotalPages(),
                finePage.isLast(),
                finePage.isFirst(),
                finePage.isEmpty()
        );
    }
}
