package dev.pranavsharma.librarymanagementsystem.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReviewRequest {

    @NotNull(message = "Book ID is mandatory!")
    private Long bookId;

    @NotNull(message = "Rating is mandatory!")
    @Min(value = 1, message = "Rating must be at least 1!")
    @Max(value = 5, message = "Rating must not exceed 5!")
    private Integer rating;

    @NotBlank(message = "Review is mandatory!")
    @Size(min = 10, max = 2000, message = "Review must be between 10 and 2000 characters!")
    private String reviewText;

    @Size(max = 200, message = "Review title must not exceed 200 characters!")
    private String title;
}
