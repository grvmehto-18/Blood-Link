package com.blood.bank.Blood.bank.controller;

import com.blood.bank.Blood.bank.dto.PasswordResetConfirmDto;
import com.blood.bank.Blood.bank.dto.PasswordResetRequestDto;
import com.blood.bank.Blood.bank.service.PasswordResetService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@ModelAttribute PasswordResetRequestDto requestDto, RedirectAttributes redirectAttributes) {
        try {
            passwordResetService.createPasswordResetToken(requestDto.getEmail());
            redirectAttributes.addFlashAttribute("sMsg", "Password reset link sent to your email.");
            return "redirect:/auth/forgot-password";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("eMsg", e.getMessage());
            return "redirect:/auth/forgot-password";
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Validate token existence and expiry (handled by service)
            passwordResetService.resetPassword(token, ""); // Just to validate token, password won't be set
            model.addAttribute("token", token);
            model.addAttribute("passwordResetConfirmDto", new PasswordResetConfirmDto());
            return "resetPassword";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("eMsg", e.getMessage());
            return "redirect:/login";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@ModelAttribute PasswordResetConfirmDto confirmDto, RedirectAttributes redirectAttributes) {
        try {
            if (!confirmDto.getNewPassword().equals(confirmDto.getConfirmPassword())) {
                redirectAttributes.addFlashAttribute("eMsg", "Passwords do not match.");
                return "redirect:/auth/reset-password?token=" + confirmDto.getToken();
            }
            passwordResetService.resetPassword(confirmDto.getToken(), confirmDto.getNewPassword());
            redirectAttributes.addFlashAttribute("sMsg", "Your password has been reset successfully. Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("eMsg", e.getMessage());
            return "redirect:/auth/reset-password?token=" + confirmDto.getToken();
        }
    }
}