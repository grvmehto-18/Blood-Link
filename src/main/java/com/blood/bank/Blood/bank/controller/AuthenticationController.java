package com.blood.bank.Blood.bank.controller;

import lombok.extern.flogger.Flogger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blood.bank.Blood.bank.dto.VerifyUserDto;
import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@Controller
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    public String postSignUp(@Valid @ModelAttribute("donor") Donor newDonor, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("donor", newDonor);
            return "donorRegistration";
        }
        try {
            Donor registeredDonor = authenticationService.signUP(newDonor);
            redirectAttributes.addFlashAttribute("email", registeredDonor.getEmail());
            redirectAttributes.addFlashAttribute("successMessage", "Verify your email: " + registeredDonor.getEmail());
            return RedirectionAndReturn.VERIFICATION_REDIRECTION;
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("eMsg", "This email or phone number is already registered.");
            return "redirect:/donorRegistration";
        }
    }

    @PostMapping("/verifyUser")
    public String postVerifyUser(@ModelAttribute VerifyUserDto verifyUserDto, RedirectAttributes redirectAttributes) {
        log.info("Is verification code is receiving : {}", verifyUserDto.getVerificationCode());
        authenticationService.verifyUser(verifyUserDto);
        redirectAttributes.addFlashAttribute("successMsg", "Account verified successfully! You can now log in.");
        return RedirectionAndReturn.LOGIN_PAGE_RED;
    }

    @PostMapping("/resendCode")
    public String resendCode(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        authenticationService.resendVerificationCode(email);
        redirectAttributes.addFlashAttribute("successMessage", "A new verification code has been sent to your email.");
        redirectAttributes.addFlashAttribute("email", email);
        return RedirectionAndReturn.VERIFICATION_REDIRECTION;
    }
}
