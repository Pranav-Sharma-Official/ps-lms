package dev.pranavsharma.librarymanagementsystem.controller;

import dev.pranavsharma.librarymanagementsystem.exception.UserException;
import dev.pranavsharma.librarymanagementsystem.payload.dto.UserDTO;
import dev.pranavsharma.librarymanagementsystem.payload.request.ForgotPasswordRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.LoginRequest;
import dev.pranavsharma.librarymanagementsystem.payload.request.ResetPasswordRequest;
import dev.pranavsharma.librarymanagementsystem.payload.response.ApiResponse;
import dev.pranavsharma.librarymanagementsystem.payload.response.AuthResponse;
import dev.pranavsharma.librarymanagementsystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(
            @Valid @RequestBody UserDTO req
            ) throws UserException {
        AuthResponse res = authService.signup(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(
            @Valid @RequestBody LoginRequest req
            ) throws UserException {
        AuthResponse res = authService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) throws UserException {
        authService.createPasswordResetToken(request.getEmail());
        ApiResponse res = new ApiResponse("A reset link has been sent to your email!", true);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestBody ResetPasswordRequest request) throws Exception {
        authService.resetPassword(request.getToken(), request.getPassword());
        ApiResponse res = new ApiResponse("Password reset successful!", true);
        return ResponseEntity.ok(res);
    }
}