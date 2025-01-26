package com.blood.bank.Blood.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String postSignUp(@ModelAttribute("donor") Donor newDonor, RedirectAttributes redirectAttributes) {
        try {
            Donor registeredDonor = authenticationService.signUP(newDonor);
            redirectAttributes.addFlashAttribute("email", registeredDonor.getEmail());
            redirectAttributes.addFlashAttribute("successMessage","Verify your email: "+registeredDonor.getEmail());
            System.out.println(redirectAttributes);
            return RedirectionAndReturn.VERIFICATION_REDIRECTION;
        } catch (Exception exception) {
            exception.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "Registration failed: " + exception.getMessage());
            return "redirect:/regiPage";
        }
    }

    @PostMapping("/verifyUser")
    public String postVerifyUser(@ModelAttribute VerifyUserDto verifyUserDto, RedirectAttributes redirectAttributes, Model model) {
            try {
                System.out.println(verifyUserDto.getVerificationCode());
                authenticationService.verifyUser(verifyUserDto);
    
                redirectAttributes.addFlashAttribute("successMsg", "Account verified successfully! You can now log in.");
                return "redirect:/login";
            } catch (RuntimeException e) {
                // On failure, reload the verification page with an error message
                redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
                redirectAttributes.addFlashAttribute("email", verifyUserDto.getEmail());
            return RedirectionAndReturn.VERIFICATION_REDIRECTION;       }
    }

    @PostMapping("/resendCode")
    public String resendVerificationCode(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            authenticationService.resendVerificationCode(email);
            redirectAttributes.addFlashAttribute("successMessage", "Verification code sent successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return RedirectionAndReturn.VERIFICATION_REDIRECTION;
        
    }

}
