package dev.pranavsharma.librarymanagementsystem.payload.request;

import dev.pranavsharma.librarymanagementsystem.domain.FineType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFineRequest {

    @NotNull(message = "Book loan ID is mandatory!")
    private Long bookLoanId;

    @NotNull(message = "Fine type is mandatory!")
    private FineType type;

    @NotNull(message = "Fine amount is mandatory!")
    @Positive(message = "Fine amount is positive!")
    private Long amount;

    private String reason;

    private String notes;
}
