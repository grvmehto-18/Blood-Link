package com.blood.bank.Blood.bank.service;

import com.blood.bank.Blood.bank.Repository.DonorRepository;
import com.blood.bank.Blood.bank.model.Donor;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final DonorRepository donorRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void createPasswordResetToken(String email) throws MessagingException {
        Donor donor = donorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Donor not found with email: " + email));

        String token = UUID.randomUUID().toString();
        donor.setResetToken(token);
        donor.setResetTokenExpiryDate(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
        donorRepository.save(donor);

        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
        emailService.sendVerificationEmail(donor.getEmail(), "Password Reset Request", "To reset your password, click here: " + resetLink);
    }

    public void resetPassword(String token, String newPassword) {
        Donor donor = donorRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired password reset token."));

        if (donor.getResetTokenExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token has expired.");
        }

        donor.setPassword(passwordEncoder.encode(newPassword));
        donor.setResetToken(null);
        donor.setResetTokenExpiryDate(null);
        donorRepository.save(donor);
    }
}