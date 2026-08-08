package dev.pranavsharma.librarymanagementsystem.payload.dto;

import dev.pranavsharma.librarymanagementsystem.domain.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotNull(message = "Email is required!")
    private String email;

    @NotNull(message = "Password is required!")
    private String password;

    @NotNull(message = "Full Name is required!")
    private String fullName;
    private String phone;
    private UserRole role;
    private String username;

    private LocalDateTime lastLogin;

}
