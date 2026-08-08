package dev.pranavsharma.librarymanagementsystem.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequest {

    @NotNull(message = "Book ID is mandatory!")
    private Long bookId;

    @Min(value = 1, message = "The values of checkout days must be at least 1!")
    private Integer checkoutDays = 14;

    private String notes;
}
