package dev.pranavsharma.librarymanagementsystem.payload.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlanDTO {

    private Long id;

    @NotBlank(message = "Plan code is mandatory!")
    private String planCode;

    @NotBlank(message = "Plan name is mandatory!")
    private String name;

    private String description;

    @NotNull(message = "Duration is mandatory!")
    @Positive(message = "The duration days must have a positive value!")
    private Integer durationDays;

    @NotNull(message = "Price is mandatory!")
    @Positive(message = "The price must have a positive value!")
    private Long price;

    private String currency;

    @NotNull(message = "Providing the value of maximum books allowed is mandatory!")
    @Positive(message = "The number of maximum books allowed must have a positive value!")
    private Integer maxBooksAllowed;

    @NotNull(message = "Providing the value of maximum days per book is mandatory!")
    @Positive(message = "The number of maximum days per book must have a positive value!")
    private Integer maxDaysPerBook;

    private Integer displayOrder;

    private Boolean isActive;

    private Boolean isFeatured;

    private String badgeText;

    private String adminNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

}
