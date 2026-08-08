package dev.pranavsharma.librarymanagementsystem.service;

import dev.pranavsharma.librarymanagementsystem.domain.FineStatus;
import dev.pranavsharma.librarymanagementsystem.domain.FineType;
import dev.pranavsharma.librarymanagementsystem.payload.dto.FineDTO;
import dev.pranavsharma.librarymanagementsystem.payload.request.CreateFineRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.WaivedFineRequest;
import dev.pranavsharma.librarymanagementsystem.payload.response.PageResponse;
import dev.pranavsharma.librarymanagementsystem.payload.response.PaymentInitiateResponse;

import java.util.List;

public interface FineService {

    FineDTO createFine(CreateFineRequest createFineRequest) throws Exception;

    PaymentInitiateResponse payFine(Long fineId, String transactionId) throws Exception;

    void markFineAsPaid(Long fineId, Long amount, String transactionId) throws Exception;

    FineDTO waivedFine(WaivedFineRequest waivedFineRequest) throws Exception;

    List<FineDTO> getMyFines(FineStatus status, FineType type) throws Exception;

    PageResponse<FineDTO> getAllFines(
            FineStatus status,
            FineType type,
            Long userId,
            int page,
            int size
    );
}
