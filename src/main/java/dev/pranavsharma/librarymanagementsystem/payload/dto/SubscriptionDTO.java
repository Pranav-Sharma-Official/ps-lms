package dev.pranavsharma.librarymanagementsystem.payload.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDTO {

    private Long id;

    @NotNull(message = "User ID is mandatory!")
    private Long userId;

    private String userName;

    private String userEmail;

    @NotNull(message = "Subscription plan ID is mandatory!")
    private Long planId;

    private String planName;

    private String planCode;

    private Long price;

    private String currency;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isActive;

    private Integer maxBooksAllowed;

    private Integer maxDaysPerBook;

    private Boolean autoRenew;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    private String notes;

    private Long daysRemaining;

    private Boolean isValid;

    private Boolean isExpired;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
