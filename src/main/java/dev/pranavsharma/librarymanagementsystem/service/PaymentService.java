package dev.pranavsharma.librarymanagementsystem.service;

import dev.pranavsharma.librarymanagementsystem.payload.dto.PaymentDTO;
import dev.pranavsharma.librarymanagementsystem.payload.request.PaymentInitiateRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.PaymentVerifyRequest;
import dev.pranavsharma.librarymanagementsystem.payload.response.PaymentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest req) throws Exception;

    PaymentDTO verifyPayment(PaymentVerifyRequest req) throws Exception;

    Page<PaymentDTO> getAllPayments(Pageable pageable);
}
