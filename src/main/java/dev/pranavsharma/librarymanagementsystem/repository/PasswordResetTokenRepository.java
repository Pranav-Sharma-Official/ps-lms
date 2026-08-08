package dev.pranavsharma.librarymanagementsystem.repository;

import dev.pranavsharma.librarymanagementsystem.model.PasswordResetToken;
import dev.pranavsharma.librarymanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

}
