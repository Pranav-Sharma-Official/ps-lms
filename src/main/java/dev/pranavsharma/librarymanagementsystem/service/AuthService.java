package dev.pranavsharma.librarymanagementsystem.service;

import dev.pranavsharma.librarymanagementsystem.exception.UserException;
import dev.pranavsharma.librarymanagementsystem.payload.dto.UserDTO;
import dev.pranavsharma.librarymanagementsystem.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String username, String password) throws UserException;
    AuthResponse signup(UserDTO userDTO) throws UserException;

    void createPasswordResetToken(String email) throws UserException;
    void resetPassword(String token, String newPassword) throws Exception;

}
