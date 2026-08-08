package dev.pranavsharma.librarymanagementsystem.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaivedFineRequest {

    @NotNull(message = "Fine ID is mandatory!")
    private Long fineId;

    @NotBlank(message = "Waiver reason is required!")
    private String reason;
}
