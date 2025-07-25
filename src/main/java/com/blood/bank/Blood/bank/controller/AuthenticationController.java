package com.blood.bank.Blood.bank.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.blood.bank.Blood.bank.dto.VerifyUserDto;
import com.blood.bank.Blood.bank.model.Donor;
import com.blood.bank.Blood.bank.service.AuthenticationService;
import com.blood.bank.Blood.bank.dto.DonorRegistrationDto;
import com.blood.bank.Blood.bank.model.Address;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@Controller
public class AuthenticationController {


    private final AuthenticationService authenticationService;

    @GetMapping("/signUp")
    public String showSignUpForm(Model model) {
        model.addAttribute("donorRegistrationDto", new DonorRegistrationDto());
        return "donorRegistration";
    }

    @PostMapping("/signUp")
    public String postSignUp(@Valid @ModelAttribute("donorRegistrationDto") DonorRegistrationDto donorRegistrationDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("donorRegistrationDto", donorRegistrationDto);
            return "donorRegistration";
        }
        try {
            // Map DTO to Donor and Address entities
            Donor newDonor = Donor.builder()
                    .fullName(donorRegistrationDto.getFullName())
                    .gender(donorRegistrationDto.getGender())
                    .dateOfBirth(donorRegistrationDto.getDateOfBirth())
                    .bloodGroup(donorRegistrationDto.getBloodGroup())
                    .phoneNumber(donorRegistrationDto.getPhoneNumber())
                    .email(donorRegistrationDto.getEmail())
                    .username(donorRegistrationDto.getUsername())
                    .password(donorRegistrationDto.getPassword())
                    .occupation(donorRegistrationDto.getOccupation())
                    .lastDonateDate(donorRegistrationDto.getLastDonateDate())
                    .hasDiseases(donorRegistrationDto.isHasDiseases())
                    .hasAllergies(donorRegistrationDto.isHasAllergies())
                    .hasCardiacConditions(donorRegistrationDto.isHasCardiacConditions())
                    .hasBleedingDisorders(donorRegistrationDto.isHasBleedingDisorders())
                    .hasHIV(donorRegistrationDto.isHasHIV())
                    .build();

            Address address = Address.builder()
                    .country(donorRegistrationDto.getCountry())
                    .state(donorRegistrationDto.getState())
                    .district(donorRegistrationDto.getDistrict())
                    .city(donorRegistrationDto.getCity())
                    .streetAddress(donorRegistrationDto.getStreetAddress())
                    .zipCode(donorRegistrationDto.getZipCode())
                    .landmark(donorRegistrationDto.getLandmark())
                    .build();
            Set<Address> addresses = new HashSet<>();
            addresses.add(address);
            newDonor.setAddresses(addresses);

            Donor registeredDonor = authenticationService.signUP(newDonor);
            redirectAttributes.addFlashAttribute("email", registeredDonor.getEmail());
            redirectAttributes.addFlashAttribute("successMessage", "Verify your email: " + registeredDonor.getEmail());
            return RedirectionAndReturn.VERIFICATION_REDIRECTION;
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("eMsg", "This email or phone number is already registered.");
            model.addAttribute("donorRegistrationDto", donorRegistrationDto);
            return "donorRegistration";
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
