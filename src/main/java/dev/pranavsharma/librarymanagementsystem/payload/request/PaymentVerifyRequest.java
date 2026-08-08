package dev.pranavsharma.librarymanagementsystem.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentVerifyRequest {

    private String razorpayPaymentId;

    private String stripePaymentIntentId;
    private String stripePaymentIntentStatus;

}
